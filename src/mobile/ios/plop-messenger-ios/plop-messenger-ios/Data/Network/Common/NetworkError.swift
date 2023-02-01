import Foundation

enum NetworkError: Error {
  case invalidURL
  case failedToCreateData
  case failedToCreateRequest
}
