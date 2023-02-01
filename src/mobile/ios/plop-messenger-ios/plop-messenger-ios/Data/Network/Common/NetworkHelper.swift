import Foundation

final class NetworkHelper {
  static func createRequest(
    path: String,
    httpMethod: String,
    httpBody: Data?,
    queries: [URLQueryItem]
  ) -> URLRequest? {
    //TODO: - BaseURL 만들기
    
    let absoluteURL = path
    var urlComponent = URLComponents(string: absoluteURL)
    urlComponent?.queryItems = queries
    
    guard let urlComponent = urlComponent,
          let url = urlComponent.url else { return nil }
    
    var request = URLRequest(url: url)
    request.httpMethod = httpMethod
    request.httpBody = httpBody
    
    return request
  }
}
