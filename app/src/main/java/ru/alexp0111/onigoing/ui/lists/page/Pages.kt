package ru.alexp0111.onigoing.ui.lists.page

enum class Pages(val description: String) {
    VIEWED("Watched"),
    ACTUAL("Currently watching"),
    PLANNED("Planned"),
    STOPPED("Stopped"),
    NOT_IN_LIST("Not tracked");

    companion object {
        const val PAGE_TAG = "PAGE_TAG"
        fun from(order: Int) = entries[order]
    }
}