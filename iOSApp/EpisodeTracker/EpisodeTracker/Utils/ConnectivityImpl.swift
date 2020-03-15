import Foundation
import Reachability
import SharedCode

class ConnectivityImpl: Connectivity {
    
    private let reachability = try! Reachability()
    private var listeners = [ConnectivityListener]()
    
    init() {
        reachability.whenReachable = { _ in
            self.notifyConnectionAvailable()
        }
        reachability.whenUnreachable = { _ in
            self.notifyConnectionUnavailable()
        }
    }
    
    func isConnected() -> Bool {
        return reachability.connection != .unavailable
    }
    
    func addListener(listener: ConnectivityListener) {
        listeners.append(listener)
        
        if listeners.count == 1 {
            startListening()
        }
    }
    
    func removeListener(listener: ConnectivityListener) {
        listeners.removeAll(where: { $0 === listener })
        
        if (listeners.isEmpty) {
            stopListening()
        }
    }
    
    private func startListening() {
        do {
            try reachability.startNotifier()
        } catch {
            log(self) { "Can't start Reachability notifier" }
        }
    }
    
    private func stopListening() {
        reachability.stopNotifier()
    }
    
    private func notifyConnectionAvailable() {
        for listener in listeners {
            listener.onConnectionAvailable()
        }
    }
    
    private func notifyConnectionUnavailable() {
        for listener in listeners {
            listener.onConnectionLost()
        }
    }
}
