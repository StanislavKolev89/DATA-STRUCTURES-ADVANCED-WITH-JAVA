import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class RoyaleArena implements IArena {

    Map<Integer, Battlecard> battleCards;
    Map<CardType, Set<Battlecard>> battleCardsByType;

    public RoyaleArena() {
        this.battleCards = new LinkedHashMap<>();
        this.battleCardsByType = new LinkedHashMap<>();
    }

    @Override
    public void add(Battlecard card) {
        if (!this.contains(card)) {
            this.battleCards.put(card.getId(), card);
        }
        this.battleCardsByType.putIfAbsent(card.getType(), new HashSet<>());
        this.battleCardsByType.get(card.getType()).add(card);


    }

    @Override
    public boolean contains(Battlecard card) {
        return this.battleCards.containsKey(card.getId());
    }

    @Override
    public int count() {
        return this.battleCards.size();
    }

    @Override
    public void changeCardType(int id, CardType type) {
        if (!this.battleCards.containsKey(id)) {
            throw new IllegalArgumentException();
        }

        Battlecard battlecard = this.battleCards.get(id);
        Set<Battlecard> battleCardsSet = this.battleCardsByType.get(battlecard.getType());
        battlecard.setType(type);
        this.battleCardsByType.putIfAbsent(type, new HashSet<>());
        this.battleCardsByType.get(type).add(battlecard);
        battleCardsSet.remove(battlecard);

    }

    @Override
    public Battlecard getById(int id) {
        if (!this.battleCards.containsKey(id)) {
            throw new UnsupportedOperationException();
        }
        return this.battleCards.get(id);
    }

    @Override
    public void removeById(int id) {
        if (!this.battleCards.containsKey(id)) {
            throw new UnsupportedOperationException();
        }
        Battlecard battlecard = this.battleCards.get(id);

        this.battleCardsByType.get(battlecard.getType()).remove(battlecard);
        this.battleCards.remove(id, battlecard);

    }

    @Override
    public Iterable<Battlecard> getByCardType(CardType type) {
        Set<Battlecard> battleCards = this.battleCardsByType.get(type);
        if (battleCards == null) {
            throw new UnsupportedOperationException();
        }
        return battleCards.stream().sorted(Comparator.comparing(Battlecard::getDamage).reversed()).sorted(Comparator.comparing(Battlecard::getId)).collect(Collectors.toList());

    }

    @Override
    public Iterable<Battlecard> getByTypeAndDamageRangeOrderedByDamageThenById(CardType type, int lo, int hi) {
        Set<Battlecard> battleCards = this.battleCardsByType.get(type);
        if (battleCards == null) {
            throw new UnsupportedOperationException();
        }
        Set<Battlecard> result = new HashSet<>();

        for (Battlecard battleCard : battleCards) {
            if (battleCard.getDamage() > lo && battleCard.getDamage() < hi) {
                result.add(battleCard);
            }
        }

        if (result.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        return getBattleCardsByDamageDescAndIdAsc(result.stream());


    }

    @Override
    public Iterable<Battlecard> getByCardTypeAndMaximumDamage(CardType type, double damage) {
        Set<Battlecard> battleCards = this.battleCardsByType.get(type);

        if (battleCards == null) {
            throw new UnsupportedOperationException();
        }
        List<Battlecard> collection = battleCards.stream().filter(c -> c.getDamage() <= damage).collect(Collectors.toList());
        if (collection.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        return getBattleCardsByDamageDescAndIdAsc(collection.stream());
    }

    @Override
    public Iterable<Battlecard> getByNameOrderedBySwagDescending(String name) {

        List<Battlecard> cardsWithName = GetBattleCardsWithSpecificName(name);

        return getBattleCardsBySwagDescAndIdAsc(cardsWithName.stream());

    }

    @Override
    public Iterable<Battlecard> getByNameAndSwagRange(String name, double lo, double hi) {

        List<Battlecard> cardsWithName = GetBattleCardsWithSpecificName(name);
        List<Battlecard> battleCardStream = cardsWithName.stream().filter(c -> c.getSwag() >= lo && c.getSwag() < hi).collect(Collectors.toList());

        return getBattleCardsBySwagDescAndIdAsc(battleCardStream.stream());
    }

    @Override
    public Iterable<Battlecard> getAllByNameAndSwag() {
        Map<String, Battlecard> cards = new LinkedHashMap<>();
        for (Battlecard card : this.battleCards.values()) {
            cards.putIfAbsent(card.getName(), card);
            Battlecard battlecard = cards.get(card.getName());
            if (battlecard.getSwag() < card.getSwag()) {
                cards.put(card.getName(), card);
            }

        }
        return cards.values();
    }

    @Override
    public Iterable<Battlecard> findFirstLeastSwag(int n) {

        if (n > this.battleCards.size()) {
            throw new UnsupportedOperationException();
        }

        return this.battleCards.values().stream().sorted((c1, c2) -> {

            int comparison = (int) Double.compare(c1.getSwag(), c2.getSwag());
            if (comparison == 0) {
                comparison = Integer.compare(c1.getId(), c2.getId());

            }
            return comparison;
        }).collect(Collectors.toList());

    }

    @Override
    public Iterable<Battlecard> getAllInSwagRange(double lo, double hi) {
        List<Battlecard> result = new ArrayList<>();
        for (Battlecard card : this.battleCards.values()) {
            if (card.getSwag() >= lo && card.getSwag() <= hi) {
                result.add(card);
            }
        }
        List<Battlecard> collect = result.stream().sorted(Comparator.comparing(Battlecard::getSwag)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public Iterator<Battlecard> iterator() {
        return this.battleCards.values().iterator();
    }


    private Iterable<Battlecard> getBattleCardsByDamageDescAndIdAsc(Stream<Battlecard> stream) {
        return stream.sorted((c1, c2) -> {
            int comparison = (int) Double.compare(c2.getDamage(), c1.getDamage());
            if (comparison == 0) {
                comparison = Integer.compare(c1.getId(), c2.getId());

            }
            return comparison;
        }).collect(Collectors.toList());
    }

    private Iterable<Battlecard> getBattleCardsBySwagDescAndIdAsc(Stream<Battlecard> stream) {

        return stream.sorted((c1, c2) -> {
            int comparison = (int) Double.compare(c2.getSwag(), c1.getSwag());
            if (comparison == 0) {
                comparison = Integer.compare(c1.getId(), c2.getId());

            }
            return comparison;
        }).collect(Collectors.toList());
    }


    private List<Battlecard> GetBattleCardsWithSpecificName(String name) {
        List<Battlecard> cardsWithName = new ArrayList<>();
        for (Battlecard card : this.battleCards.values()) {
            if (card.getName().equals(name)) {
                cardsWithName.add(card);
            }
        }

        if (cardsWithName.isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return cardsWithName;
    }


}
