package com.dan323.functional.data.sql;

import com.dan323.functional.data.function.Reader;

import java.util.function.Function;

public record Config(String host, int port, String db) {

    static Function<Config,String> getPath = Reader.<Config>getInstance().liftA2(
            (url, db) -> url + "/" + db,
            Reader.<Config>getInstance()
                    .liftA2((host, port) -> "jdbc:postgresql://" + host + ":" + port, Config::host, Config::port),
            Config::db);
}
