import Foundation
import IGListKit
import SharedCode

extension ToWatchShowViewModel: ListDiffable {
    public func diffIdentifier() -> NSObjectProtocol {
        return id as NSObjectProtocol
    }
    
    public func isEqual(toDiffableObject object: ListDiffable?) -> Bool {
        if let object = object as? ToWatchShowViewModel {
            return self == object
        }
        return false
    }
}
