module functional.data {
    requires functional.annotations;
    opens com.dan323.functional.data.continuation to functional.annotations;
    opens com.dan323.functional.data.either to functional.annotations;
    opens com.dan323.functional.data.function to functional.annotations;
    opens com.dan323.functional.data.list to functional.annotations;
    opens com.dan323.functional.data.list.zipper to functional.annotations;
    opens com.dan323.functional.data.optional to functional.annotations;
    opens com.dan323.functional.data.tree to functional.annotations;
    opens com.dan323.functional.data.bool to functional.annotations;
    opens com.dan323.functional.data.integer to functional.annotations;
    opens com.dan323.functional.data.pair to functional.annotations;
    opens com.dan323.functional.data.util.applicative to functional.annotations;
}