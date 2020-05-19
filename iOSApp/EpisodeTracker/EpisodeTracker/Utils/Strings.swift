import SharedCode

struct R {
    static let str = MR.strings()
}

func string(_ res: ResourcesStringResource) -> String {
    return StringsKt.string(res: res).localized()
}
