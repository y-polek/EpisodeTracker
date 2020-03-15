import Foundation
import SharedCode

func log(_ receiver: Any, message: @escaping () -> String) {
    let tag = String(describing: type(of: receiver))
    LogKt.log(receiver, tag: tag, message: message)
}
