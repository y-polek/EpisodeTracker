import Foundation
import IGListDiffKit
import SharedCode

extension ToWatchShowViewModel: ListDiffable {
    
    public func diffIdentifier() -> NSObjectProtocol {
        let identifier: Int32
        if isSpecials {
            identifier = -id
        } else {
            identifier = id
        }
        return identifier as NSObjectProtocol
    }
    
    public func isEqual(toDiffableObject object: ListDiffable?) -> Bool {
        if let object = object as? ToWatchShowViewModel {
            return self == object
        }
        return false
    }
}
