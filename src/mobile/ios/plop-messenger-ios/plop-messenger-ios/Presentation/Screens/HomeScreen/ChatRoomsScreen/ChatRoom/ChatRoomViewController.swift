import UIKit
import RxSwift
import RxCocoa
import MessageKit
import RxDataSources
import InputBarAccessoryView
import SwiftStomp

final class ChatRoomViewController: MessagesViewController {
  private var chatRoom: ChatRoom
  private let currentUserID: String
  private var viewModel: ChatRoomViewModel
  
  private let sendTrigger = PublishSubject<String>()
  private let createRoomTrigger = PublishSubject<ChatRoom>()
  private let fetchHistoryTrigger = PublishSubject<Void>()
  private let fetchNewMessagesTrigger = PublishSubject<String?>()
  private let saveLastMessageTrigger = PublishSubject<String>()
  
  private let disposeBag = DisposeBag()
  private let senderID = UserDefaults.standard.string(forKey: "currentUserID")
  
  var messages = [Message]() {
    didSet {
      self.messages.sort()
      self.messagesCollectionView.reloadData()
      self.messagesCollectionView.scrollToLastItem(animated: true)
    }
  }
  private let swiftStomp: SwiftStomp?
  
  init(_ room: ChatRoom, _ currentUserID: String, _ swiftStomp: SwiftStomp) {
    self.chatRoom = room
    self.currentUserID = currentUserID
    self.swiftStomp = swiftStomp
    self.viewModel = ChatRoomViewModel(room)
    super.init(nibName: nil, bundle: nil)
    hidesBottomBarWhenPushed = true
  }
  
  init(_ room: ChatRoom, _ currentUserID: String) {
    self.chatRoom = room
    self.currentUserID = currentUserID
    self.swiftStomp = nil
    self.viewModel = ChatRoomViewModel(room)
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
      fetchMessageHistoryTrigger: fetchHistoryTrigger.asDriverOnErrorJustComplete(),
      fetchNewMessagesTrigger: fetchNewMessagesTrigger.asDriverOnErrorJustComplete(),
      createRoomTrigger: createRoomTrigger.asDriverOnErrorJustComplete(),
      sendTrigger: sendTrigger.asDriver(onErrorJustReturn: ""),
      saveLastMessageTrigger: saveLastMessageTrigger.asDriverOnErrorJustComplete())
    
    let output = viewModel.transform(input)
    
    output.messageHistory
      .drive(onNext: { [unowned self] messages in
        self.messages = messages
        self.fetchNewMessagesTrigger.onNext(messages.last?.messageID)
      })
      .disposed(by: disposeBag)
    
    output.fetchNewMessages
      .drive()
      .disposed(by: disposeBag)
    
    output.createRoom
      .drive(onNext: { [unowned self] chatRoom in
        self.chatRoom = chatRoom
        self.viewModel = ChatRoomViewModel(chatRoom)
        if let swiftStomp = self.swiftStomp {
          swiftStomp.subscribe(to: "/chatting/topic/room/\(chatRoom.roomID)")
        }
      })
      .disposed(by: disposeBag)
    
    output.chatRoomListener
      .drive(onNext: { chatRoom in
        self.messages = chatRoom.messages
      })
      .disposed(by: disposeBag)
    
    output.saveLastMessage
      .drive()
      .disposed(by: disposeBag)
  }
}

extension ChatRoomViewController: MessagesDataSource {
  var currentSender: MessageKit.SenderType {
    return Member(
      userID: senderID ?? "",
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
}

extension ChatRoomViewController: InputBarAccessoryViewDelegate {
  func inputBar(
    _ inputBar: InputBarAccessoryView,
    didPressSendButtonWith text: String
  ) {
    //If send triggered but don't have roomID, create request
    if chatRoom.roomID == "" {
      createRoomTrigger.onNext(chatRoom)
    } else {
      guard let swiftStomp = swiftStomp else { return }
      let messageRequest = MessageRequest(
        roomID: chatRoom.roomID,
        senderID: senderID ?? "",
        messageType: "TEXT",
        content: text,
        createdAt: "\(Date())"
      )
      swiftStomp.send(
        body: messageRequest,
        to: "/chatting/topic/room/\(chatRoom.roomID)")
      inputBar.inputTextView.text.removeAll()
      sendTrigger.onNext(text)
    }
  }
}

//MARK: - UI Setup
extension ChatRoomViewController {
  private func configureUI() {
    title = self.chatRoom.title ?? "채팅방"
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
