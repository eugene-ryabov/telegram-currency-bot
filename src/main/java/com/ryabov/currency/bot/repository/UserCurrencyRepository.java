package com.ryabov.currency.bot.repository;

import com.ryabov.currency.bot.entity.UserCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCurrencyRepository extends JpaRepository<UserCurrency, Long> {
    List<UserCurrency> findAllByUserId(long userId);
}
