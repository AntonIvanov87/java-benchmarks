package ru.hh.antonivanov;

import java.time.LocalDate;

public class User {

  public int id;
  public String firstName;
  public String middleName;
  public String lastName;
  public LocalDate birthDate;

  @Override
  public String toString() {
    return "User{" +
               "id=" + id +
               ", firstName='" + firstName + '\'' +
               ", middleName='" + middleName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", birthDate=" + birthDate +
               '}';
  }
}
