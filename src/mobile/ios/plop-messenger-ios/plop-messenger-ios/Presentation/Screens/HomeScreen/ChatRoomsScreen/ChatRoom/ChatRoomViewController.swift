import UIKit
import RxSwift
import RxCocoa
import MessageKit
import InputBarAccessoryView
import SwiftStomp

final class ChatRoomViewController: MessagesViewController {

  //MARK: - Triggers
  private let createChatRoomTrigger = PublishSubject<ChatRoom>()
  private let subscribeRoomTrigger = PublishSubject<Void>()
  private let sendFirstMessageTrigger = PublishSubject<String>()
  private let fetchHistoryTrigger = PublishSubject<Void>()
  private let fetchNewMessagesTrigger = PublishSubject<String>()
  private let sendTrigger = PublishSubject<String>()
  private let saveLastMessageTrigger = PublishSubject<String>()
  
  private let disposeBag = DisposeBag()
  
  private var chatRoom: ChatRoom? {
    didSet {
      self.title = chatRoom?.title
    }
  }
  private var firstMessage: String? = nil
  
  var viewModel: ChatRoomViewModel!
  var messages = [Message]() {
    didSet {
      self.messages.sort()
      self.messagesCollectionView.reloadData()
      self.messagesCollectionView.scrollToLastItem(animated: true)
    }
  }
  
  init() {
    super.init(nibName: nil, bundle: nil)
    hidesBottomBarWhenPushed = true
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  override func viewDidLoad() {
    super.viewDidLoad()
    view.backgroundColor = .systemBackground
    configureUI()
    bind()
  }
  
  override func viewWillAppear(_ animated: Bool) {
    super.viewWillAppear(animated)
    self.chatRoom = viewModel.currentRoom
  }
  
  override func viewDidAppear(_ animated: Bool) {
    super.viewDidAppear(animated)
    fetchHistoryTrigger.onNext(())
  }
  
  override func viewWillDisappear(_ animated: Bool) {
    super.viewWillDisappear(animated)
    guard let message = messages.last else {
      return
    }
    saveLastMessageTrigger.onNext(message.messageID)
  }
  
  private func bind() {
    
    let input = ChatRoomViewModel.Input(
      createChatRoomTrigger: createChatRoomTrigger.asDriverOnErrorJustComplete(),
      subscribeRoomTrigger: subscribeRoomTrigger.asDriverOnErrorJustComplete(),
      sendFirstMessageTrigger: sendFirstMessageTrigger.asDriverOnErrorJustComplete(),
      fetchMessageHistoryTrigger: fetchHistoryTrigger.asDriverOnErrorJustComplete(),
      fetchNewMessagesTrigger: fetchNewMessagesTrigger.asDriverOnErrorJustComplete(),
      sendTrigger: sendTrigger.asDriverOnErrorJustComplete(),
      saveLastMessageTrigger: saveLastMessageTrigger.asDriverOnErrorJustComplete())

    let output = viewModel.transform(input)
    
    output.createdRoom
      .drive(onNext: { [weak self] chatRoom in
        guard let self = self else { return }
        self.chatRoom = chatRoom
        self.subscribeRoomTrigger.onNext(())
      })
      .disposed(by: disposeBag)
    
    output.subscribedRoom
      .drive(onNext: { [weak self] in
        guard let self = self,
              let firstMessage = self.firstMessage else { return }
        self.sendFirstMessageTrigger.onNext(firstMessage)
      })
      .disposed(by: disposeBag)
    
    output.firstMessage.drive().disposed(by: disposeBag)
    
    output.messageHistory
      .drive(onNext: { [weak self] messages in
        guard let self = self else { return }
        self.messages = messages
        guard let lastMessage = messages.last else { return }
        self.fetchNewMessagesTrigger.onNext(lastMessage.messageID)
      })
      .disposed(by: disposeBag)
    
    output.fetchNewMessages
      .drive()
      .disposed(by: disposeBag)

    output.chatRoomListener
      .drive(onNext: { chatRoom in
        self.messages = chatRoom.messages
      })
      .disposed(by: disposeBag)
    
    output.messageSended
      .drive()
      .disposed(by: disposeBag)
    
    output.saveLastMessage
      .drive()
      .disposed(by: disposeBag)
  }
}

extension ChatRoomViewController: MessagesDataSource {
  var currentSender: MessageKit.SenderType {
    return Member(
      userID: viewModel.currentUserID,
      lastReadMessageID: nil,
      enteredAt: "")
  }
  
  func numberOfSections(in messagesCollectionView: MessagesCollectionView) -> Int {
    return messages.count
  }
  
  func messageForItem(
    at indexPath: IndexPath,
    in messagesCollectionView: MessagesCollectionView
  ) -> MessageType {
    return messages[indexPath.section]
  }
  
  func messageTopLabelAttributedText(
    for message: MessageType,
    at indexPath: IndexPath
  ) -> NSAttributedString? {
    let name = message.sender.displayName
    return NSAttributedString(
      string: name,
      attributes: [
        .font: UIFont.preferredFont(forTextStyle: .caption1),
        .foregroundColor: UIColor(white: 0.3, alpha: 1)
      ])
  }
}

extension ChatRoomViewController: MessagesLayoutDelegate {
  func footerViewSize(for section: Int, in messagesCollectionView: MessagesCollectionView) -> CGSize {
    return CGSize(width: 0, height: 8)
  }
  
  func messageTopLabelHeight(for message: MessageType, at indexPath: IndexPath, in messagesCollectionView: MessagesCollectionView) -> CGFloat {
    return 16
  }
}

extension ChatRoomViewController: MessagesDisplayDelegate {
  func backgroundColor(for message: MessageType, at indexPath: IndexPath, in messagesCollectionView: MessagesCollectionView) -> UIColor {
    return isFromCurrentSender(message: message) ? UIConstants.plopColor : .systemGray
  }
  
  func textColor(for message: MessageType, at indexPath: IndexPath, in messagesCollectionView: MessagesCollectionView) -> UIColor {
    return isFromCurrentSender(message: message) ? .white : .label
  }
  
  func messageStyle(for message: MessageType, at indexPath: IndexPath, in messagesCollectionView: MessagesCollectionView) -> MessageStyle {
    let cornerDirection: MessageStyle.TailCorner = isFromCurrentSender(
      message: message) ? .bottomRight : .bottomLeft
    return .bubbleTail(cornerDirection, .curved)
  }
  
  func configureAvatarView(_ avatarView: AvatarView, for message: MessageType, at indexPath: IndexPath, in messagesCollectionView: MessagesCollectionView) {
    avatarView.image = UIImage(named: "base-profile")
  }
}

extension ChatRoomViewController: InputBarAccessoryViewDelegate {
  func inputBar(
    _ inputBar: InputBarAccessoryView,
    didPressSendButtonWith text: String
  ) {
    //If send triggered but don't have roomID, create request
    let chatRoom = viewModel.currentRoom
    if chatRoom.roomID == "" {
      createChatRoomTrigger.onNext(chatRoom)
    } else {
      inputBar.inputTextView.text.removeAll()
      sendTrigger.onNext(text)
    }
  }
}

//MARK: - UI Setup
extension ChatRoomViewController {
  private func configureUI() {
    messagesCollectionView.messagesDataSource = self
    messagesCollectionView.messagesLayoutDelegate = self
    messagesCollectionView.messagesDisplayDelegate = self
    messageInputBar.delegate = self
    messageInputBar.sendButton.setTitle("전송", for: .normal)
    messageInputBar.sendButton.setTitleColor(UIConstants.plopColor,
                                             for: .normal)
    messageInputBar.inputTextView.autocorrectionType = .no
    messageInputBar.inputTextView.autocapitalizationType = .none
    messageInputBar.tintColor = UIConstants.plopColor
  }
}
