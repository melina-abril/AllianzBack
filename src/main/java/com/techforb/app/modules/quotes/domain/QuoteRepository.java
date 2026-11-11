package com.techforb.app.modules.quotes.domain;

import java.util.Optional;

public interface QuoteRepository {

    Optional<Quote> findById(String id);

    Quote save(Quote quote);
}
