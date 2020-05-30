import SharedCode

struct R {
    static let str = MR.strings()
}

func string(_ res: ResourcesStringResource) -> String {
    return StringsKt.string(res: res).localized()
}

func string(_ res: ResourcesStringResource, _ args: Any...) -> String {
    return StringsKt.string(res: res, args: args).localized()
}
