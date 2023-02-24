import Foundation

enum NetworkError: Error {
  case invalidURL
  case failedToCreateData
  case failedToCreateRequest
  
  //Auth
  case failedToLogin
  case failedToAutoLogin
  case failedToSignup
  case invalidVerifyCode
  case failedWithdrawal
  case failedToLogout
  
  // User
  case failedFetchUserInfo
  case failedToUpdateUserInfo
  case failedToSearchUser
  
  // Friend
  case failedToFetchFriendList
  case failedToRequestFriend
  case failedToFetchFriendRequestList
  case failedToDeleteFriend
  case failedToRespondToRequest
  case failedToFetchRequestSendedList
  
  //Room
  case failedToCreateChatRoom
  case failedToInvite
  case failedToFetchChatRooms
  case failedToLeaveChatRoom
  case failedToFetchChatRoomInfo
  
  //Message
  case failedToFetchUnreadMessages
}
