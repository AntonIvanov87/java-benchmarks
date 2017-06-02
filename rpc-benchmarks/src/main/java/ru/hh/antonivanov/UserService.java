package ru.hh.antonivanov;

import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class UserService {

  User generate(int sleepMs) throws InterruptedException {
    Thread.sleep(sleepMs);

    Random random = ThreadLocalRandom.current();

    User user = new User();
    user.id = random.nextInt(10);
    user.firstName = "First" + random.nextInt(10);
    user.middleName = "Middle" + random.nextInt(10);
    user.lastName = "Last" + random.nextInt(10);

    int year = 1960 + random.nextInt(50);
    int month = 1+random.nextInt(12);
    int day = 1+random.nextInt(28);
    user.birthDate = LocalDate.of(year, month, day);

    return user;
  }

}
