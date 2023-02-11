import Foundation

enum UseCaseError: Error {
  case invalidResponse
  case failedToFetchToken
  case invalidURL
  case failedToSendMessage
  case failedToFetchEmail
}
