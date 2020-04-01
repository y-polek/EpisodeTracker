import Foundation
import IGListDiffKit
import SharedCode

extension MyShowsListItem.ShowViewModel: ListDiffable {
    
    public func diffIdentifier() -> NSObjectProtocol {
        return id as NSObjectProtocol
    }
    
    public func isEqual(toDiffableObject object: ListDiffable?) -> Bool {
        if let object = object as? MyShowsListItem.ShowViewModel {
            return self == object
        }
        return false
    }
}
