import Foundation

final class NetworkHelper {
  static func createRequest(
    path: String,
    httpMethod: String,
    httpBody: Data?,
    queries: [URLQueryItem]
  ) -> URLRequest? {
    let absoluteURL = "http://3.39.130.186:8000" + path
    var urlComponent = URLComponents(string: absoluteURL)
    urlComponent?.queryItems = queries
    
    guard let urlComponent = urlComponent,
          let url = urlComponent.url else { return nil }
    
    var request = URLRequest(url: url)
    request.httpMethod = httpMethod
    request.httpBody = httpBody
    request.setValue("application/json", forHTTPHeaderField: "Content-Type")
    return request
  }
}
