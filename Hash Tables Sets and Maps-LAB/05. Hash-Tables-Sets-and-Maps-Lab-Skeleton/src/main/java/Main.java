public class Main {

    public static void main(String[] args) {


        HashTable<Integer,String > hash = new HashTable<>();
        hash.add(12,"Pesho");
        hash.add(13,"Gosho");
//        hash.add(13,"Maria");

        System.out.println(hash.size());
    }
}
