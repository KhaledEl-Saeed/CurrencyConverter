CREATE TABLE IF NOT EXISTS currency_convertor (
                                    id SERIAL PRIMARY KEY,
                                    timestamp BIGINT NOT NULL,
                                    currency VARCHAR(3) NOT NULL,
                                    rates TEXT NOT NULL
);

