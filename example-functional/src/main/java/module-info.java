module functional.data {
    requires functional.annotations;
    opens com.dan323.functional.data.continuation to functional.annotations;
    opens com.dan323.functional.data.either to functional.annotations;
    opens com.dan323.functional.data.function to functional.annotations;
    opens com.dan323.functional.data.list to functional.annotations;
    opens com.dan323.functional.data.optional to functional.annotations;
    opens com.dan323.functional.data.tree to functional.annotations;
}