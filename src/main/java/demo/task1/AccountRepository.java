package demo.task1;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountRepository {
    /**
     * Tworzenie nowego konta
     * @param name nazwa wlasciciela
     * @param address adres wlasciciela
     * @param balance poczatkowy stan konta
     * @return obiekt konta z id nadanym przez repozytorium
     */
    Account create(String name, String address, BigDecimal balance);

    /**
     * Nadpisuje dane konta o identyfikatorze account.id w repozytorium
     * @param account obiekt konta z id nadanym przez repozytorium, ktorego stan nadpisze stan obiektu w repozytorium
     * @throws IllegalArgumentException gdy id to null lub brak konta o opodanym id w repozytorium
     */
    void save(Account account);

    /**
     * Znajduje obiekt o podanym id
     * @param id identyfikator konta
     * @return obiekt konta w optional lub Optional.EMPTY gdy brak konta o podanym id
     * @throws IllegalArgumentException gdy id ma wartosc null
     */
    Optional<Account> findById(Long id);

    /**
     * Znajduje konto o podanych danych
     * @param name nazwa wlasciciela
     * @param address adres wlasciciela
     * @return obiekt konta w optional lub Optional.EMPTY gdy brak konta o podanych danych
     */
    Optional<Account> findByNameAndAddress(String name, String address);
}
