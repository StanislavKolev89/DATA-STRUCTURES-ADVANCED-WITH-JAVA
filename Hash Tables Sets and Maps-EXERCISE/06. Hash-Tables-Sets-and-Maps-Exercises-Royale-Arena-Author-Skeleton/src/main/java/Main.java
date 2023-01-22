public class Main {
    public static void main(String[] args) {
        RoyaleArena royaleArena = new RoyaleArena();
        Battlecard battleCard = new Battlecard(1,CardType.MELEE,"Card1",30,30);
        Battlecard battleCard2 = new Battlecard(2,CardType.MELEE,"Card1",40,40);
        Battlecard battleCard3 = new Battlecard(3,CardType.MELEE,"Card2",40,50);
        Battlecard battleCard4 = new Battlecard(4,CardType.MELEE,"Card2",40,60);
        royaleArena.add(battleCard);
        royaleArena.add(battleCard2);
        royaleArena.add(battleCard3);
        royaleArena.add(battleCard4);
        Iterable<Battlecard> allByNameAndSwag1 = royaleArena.getAllByNameAndSwag();


        Iterable<Battlecard> byTypeAndDamageRangeOrderedByDamageThenById = royaleArena.getByTypeAndDamageRangeOrderedByDamageThenById(CardType.MELEE, 20, 42);

//        Iterable<Battlecard> byCardTypeAndMaximumDamage = royaleArena.getByCardTypeAndMaximumDamage(CardType.MELEE, 40);

        Iterable<Battlecard> card1 = royaleArena.getByNameOrderedBySwagDescending("Card1");

        Iterable<Battlecard> card2 = royaleArena.getByNameAndSwagRange("Card1", 40, 60);

        Iterable<Battlecard> allByNameAndSwag = royaleArena.getAllByNameAndSwag();

        Iterable<Battlecard> firstLeastSwag = royaleArena.findFirstLeastSwag(3);

        royaleArena.removeById(1);
        royaleArena.removeById(2);
        royaleArena.removeById(3);
        royaleArena.removeById(4);
        Iterable<Battlecard> allInSwagRange = royaleArena.getAllInSwagRange(100, 200);

        Iterable<Battlecard> byCardType = royaleArena.getByCardTypeAndMaximumDamage(CardType.SPELL,20);



    }
}
