import java.util.concurrent.*;

public class Main {

    public static final int CARS_COUNT = 4;
    public static boolean isWinner = false;

    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }

        ExecutorService service = Executors.newFixedThreadPool(CARS_COUNT);
        CountDownLatch cdlStart = new CountDownLatch(CARS_COUNT);
        CountDownLatch cdlFinish = new CountDownLatch(CARS_COUNT);

        for (int i = 0; i < cars.length; i++) {
            final int finalI = i;
            new Thread(()-> {
                cars[finalI].preparation();
                cdlStart.countDown();
            }).start();
        }

        try {
            cdlStart.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");


        for (int i = 0; i < cars.length; i++) {
            final int finalI = i;
            service.execute(() -> {
                cars[finalI].run();
                if (!isWinner) {
                    isWinner = true;
                    System.out.println(cars[finalI].getName()+ " победитель!");
                }
                cdlFinish.countDown();
            });
        }
        service.shutdown();

        try {
            cdlFinish.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }

}
