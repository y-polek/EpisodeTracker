import UIKit
import IGListDiffKit

extension ListIndexPathResult {
    
    func apply(_ tableView: UITableView, deletedSections: [Int], insertedSections: [Int]) {
        tableView.beginUpdates()
        tableView.deleteSections(IndexSet(deletedSections), with: .fade)
        tableView.insertSections(IndexSet(insertedSections), with: .fade)
        tableView.deleteRows(at: self.deletes, with: .fade)
        tableView.insertRows(at: self.inserts, with: .automatic)
        tableView.reloadRows(at: self.updates, with: .automatic)
        tableView.endUpdates()
    }
}

struct SectionDiff {
    let deleted: [Int]
    let inserted: [Int]
    
    static func diff(oldIndex: Int, newIndex: Int) -> SectionDiff {
        var deletedSections = [Int]()
        var insertedSections = [Int]()
        
        if oldIndex >= 0 && newIndex < 0 {
            deletedSections.append(oldIndex)
        }
        if oldIndex < 0 && newIndex >= 0 {
            insertedSections.append(newIndex)
        }
        
        return SectionDiff(deleted: deletedSections, inserted: insertedSections)
    }
}
