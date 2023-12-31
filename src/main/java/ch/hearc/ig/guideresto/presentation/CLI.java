package ch.hearc.ig.guideresto.presentation;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toUnmodifiableSet;

import ch.hearc.ig.guideresto.business.*;
import ch.hearc.ig.guideresto.persistence.*;

import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CLI {

  private final Scanner scanner;
  private final PrintStream printStream;
  private final RestaurantService restaurantService;
  private final RestaurantTypeService restaurantTypeService;
  private final BasicEvaluationService basicEvaluationService;
  private final CompleteEvaluationService completeEvaluationService;
  private final EvaluationCriteriaService evaluationCriteriaService;
  private final GradeService gradeService;

  // Injection de dépendances
  public CLI(Scanner scanner, PrintStream printStream, RestaurantService restaurantService, CityService cityService, RestaurantTypeService restaurantTypeService, BasicEvaluationService basicEvaluationService, CompleteEvaluationService completeEvaluationService, EvaluationCriteriaService evaluationCriteriaService, GradeService gradeService) {
    this.scanner = scanner;
    this.printStream = printStream;
    this.restaurantService = restaurantService;
    this.restaurantTypeService = restaurantTypeService;
    this.basicEvaluationService = basicEvaluationService;
    this.completeEvaluationService = completeEvaluationService;
    this.evaluationCriteriaService = evaluationCriteriaService;
    this.gradeService = gradeService;
  }

  public void start() {
    println("Bienvenue dans GuideResto ! Que souhaitez-vous faire ?");
    int choice;
    do {
      printMainMenu();
      choice = readInt();
      proceedMainMenu(choice);
    } while (choice != 0);
  }

  private void printMainMenu() {
    println("======================================================");
    println("Que voulez-vous faire ?");
    println("1. Afficher la liste de tous les restaurants");
    println("2. Rechercher un restaurant par son nom");
    println("3. Rechercher un restaurant par ville");
    println("4. Rechercher un restaurant par son type de cuisine");
    println("5. Saisir un nouveau restaurant");
    println("0. Quitter l'application");
  }

  private void proceedMainMenu(int choice) {
    switch (choice) {
      case 1:
        showRestaurantsList();
        break;
      case 2:
        searchRestaurantByName();
        break;
      case 3:
        searchRestaurantByCity();
        break;
      case 4:
        searchRestaurantByType();
        break;
      case 5:
        addNewRestaurant();
        break;
      case 0:
        println("Au revoir !");
        break;
      default:
        println("Erreur : saisie incorrecte. Veuillez réessayer");
        break;
    }
  }

  private Optional<Restaurant> pickRestaurant(Set<ch.hearc.ig.guideresto.business.Restaurant> restaurants) {
    if (restaurants.isEmpty()) {
      println("Aucun restaurant n'a été trouvé !");
      return Optional.empty();
    }

    String restaurantsText = restaurants.stream()
        .map(r -> "\"" + r.getName() + "\" - " + r.getStreet() + " - "
            + r.getZipCode() + " " + r.getCityName())
        .collect(joining("\n", "", "\n"));

    println(restaurantsText);
    println(
        "Veuillez saisir le nom exact du restaurant dont vous voulez voir le détail, ou appuyez sur Enter pour revenir en arrière");
    String choice = readString();

    return searchRestaurantByName(restaurants, choice);
  }

  private void showRestaurantsList() {
    println("Liste des restaurants : ");

    RestaurantService restaurantService = new RestaurantService();
    Set<Restaurant> restaurants = new HashSet<>(restaurantService.findAll());

    Optional<Restaurant> maybeRestaurant = pickRestaurant(restaurants);
    // Si l'utilisateur a choisi un restaurant, on l'affiche, sinon on ne fait rien et l'application va réafficher le menu principal
    maybeRestaurant.ifPresent(this::showRestaurant);
  }

  private void searchRestaurantByName() {
    println("Veuillez entrer une partie du nom recherché : ");
    String research = readString();
    RestaurantService restaurantService = new RestaurantService();

    Set<Restaurant> restaurants = new HashSet<>(restaurantService.findRestaurantByName(research));

    Optional<Restaurant> maybeRestaurant = pickRestaurant(restaurants);
    maybeRestaurant.ifPresent(this::showRestaurant);
  }

  /**
   * Affiche une liste de restaurants dont le nom de la ville contient une chaîne de caractères
   * saisie par l'utilisateur
    */
  private void searchRestaurantByCity() {
    println("Veuillez entrer une partie du nom de la ville désirée : ");
    String research = readString();

    RestaurantService restaurantService = new RestaurantService();
    List <Restaurant> foundRestaurants = restaurantService.findRestaurantsByCityName(research);
    Set <Restaurant> restaurants = new HashSet<>(foundRestaurants);

    Optional<Restaurant> maybeRestaurant = pickRestaurant(restaurants);
    maybeRestaurant.ifPresent(this::showRestaurant);
  }

  private City pickCity(Set<City> cities) {
    println(
        "Voici la liste des villes possibles, veuillez entrer le NPA de la ville désirée : ");

    cities.forEach((currentCity) -> System.out
        .println(currentCity.getZipCode() + " " + currentCity.getCityName()));
    println("Entrez \"NEW\" pour créer une nouvelle ville");
    String choice = readString();

    if (choice.equalsIgnoreCase("NEW")) {
      return createNewCity();
    }else{
      CityService cityService = new CityService();
      return cityService.findCityByZipCode(choice)
              .orElseGet(() -> {
                println("Aucune ville trouvée avec ce NPA. Veuillez réessayer.");
                return pickCity(cities);
              });
    }
  }

  private City createNewCity() {
    println("Veuillez entrer le NPA de la nouvelle ville : ");
    String zipCode = readString();
    println("Veuillez entrer le nom de la nouvelle ville : ");
    String cityName = readString();

    City newCity = new City(null, zipCode, cityName);

    CityService cityService = new CityService();
    cityService.saveOrUpdate(newCity);

    return newCity;
  }

  private RestaurantType pickRestaurantType(Set<RestaurantType> types) {

    String typesText = types.stream()
        .map(currentType -> "\"" + currentType.getLabel() + "\" : " + currentType.getDescription())
        .collect(joining("\n"));

    println(
        "Voici la liste des types possibles, veuillez entrer le libellé exact du type désiré : ");
    println(typesText);
    String choice = readString();

    Optional<RestaurantType> maybeRestaurantType = searchTypeByLabel(types, choice);
    return maybeRestaurantType.orElseGet(() -> pickRestaurantType(types));
  }

  private void searchRestaurantByType() {
    RestaurantTypeService restaurantTypeService = new RestaurantTypeService();
    Set<RestaurantType> restaurantTypes = new HashSet<>(restaurantTypeService.findAll());
    RestaurantType chosenType = pickRestaurantType(restaurantTypes);

    if (chosenType != null) {
      RestaurantService restaurantService = new RestaurantService();
      Set<Restaurant> restaurants = new HashSet<>(restaurantService.findRestaurantByType(chosenType));

      Optional<Restaurant> maybeRestaurant = pickRestaurant(restaurants);
      maybeRestaurant.ifPresent(this::showRestaurant);
    } else {
      println("Aucun type de restaurant sélectionné.");
    }
  }

  private void addNewRestaurant() {
    println("Vous allez ajouter un nouveau restaurant !");
    println("Quel est son nom ?");
    String name = readString();
    println("Veuillez entrer une courte description : ");
    String description = readString();
    println("Veuillez entrer l'adresse de son site internet : ");
    String website = readString();
    println("Rue : ");
    String street = readString();

    CityService cityService = new CityService();
    City city = pickCity(new HashSet<>(cityService.findAll()));

    RestaurantTypeService restaurantTypeService = new RestaurantTypeService();
    RestaurantType restaurantType = pickRestaurantType(new HashSet<>(restaurantTypeService.findAll()));

    if (city != null && restaurantType != null) {
      // Assuming Localisation class has a constructor that takes a street and a city
      Localisation address = new Localisation(street, city);

      RestaurantService restaurantService = new RestaurantService();
      Restaurant restaurant = new Restaurant(null, name, description, website, address, restaurantType);
      restaurantService.saveOrUpdate(restaurant);

      showRestaurant(restaurant);
    } else {
      println("Création du restaurant annulée.");
    }  }

  private void showRestaurant(Restaurant restaurant) {
    String sb = restaurant.getName() + "\n" +
        restaurant.getDescription() + "\n" +
        restaurant.getType().getLabel() + "\n" +
        restaurant.getWebsite() + "\n" +
        restaurant.getAddress().getStreet() + ", " +
        restaurant.getAddress().getCity().getZipCode() + " " + restaurant.getAddress().getCity()
        .getCityName() + "\n" +
        "Nombre de likes : " + countLikes(restaurant.getEvaluations(), true) + "\n" +
        "Nombre de dislikes : " + countLikes(restaurant.getEvaluations(), false) + "\n" +
        "\nEvaluations reçues : " + "\n";

    String text = restaurant.getEvaluations()
        .stream()
        .filter(CompleteEvaluation.class::isInstance)
        .map(CompleteEvaluation.class::cast)
        .map(this::getCompleteEvaluationDescription)
        .collect(joining("\n"));

    println("Affichage d'un restaurant : ");
    println(sb);
    println(text);

    int choice;
    do { // Tant que l'utilisateur n'entre pas 0 ou 6, on lui propose à nouveau les actions
      showRestaurantMenu();
      choice = readInt();
      proceedRestaurantMenu(choice, restaurant);
    } while (choice != 0 && choice != 6); // 6 car le restaurant est alors supprimé...
  }

  private long countLikes(Set<Evaluation> evaluations, boolean likeRestaurant) {
    return evaluations.stream()
        .filter(BasicEvaluation.class::isInstance)
        .map(BasicEvaluation.class::cast)
        .filter(eval -> likeRestaurant == eval.isLikeRestaurant())
        .count();
  }

  private String getCompleteEvaluationDescription(CompleteEvaluation eval) {
    StringBuilder result = new StringBuilder();
    result.append("Evaluation de : ").append(eval.getUsername()).append("\n");
    result.append("Commentaire : ").append(eval.getComment()).append("\n");


    Set<Grade> grades = this.gradeService.findByEvaluation(eval.getId());
    for (Grade grade : grades) {
      result.append(grade.getCriteria().getName())
              .append(" : ")
              .append(grade.getGrade())
              .append("/5\n");
    }

    return result.toString();
  }

  private void showRestaurantMenu() {
    println("======================================================");
    println("Que souhaitez-vous faire ?");
    println("1. J'aime ce restaurant !");
    println("2. Je n'aime pas ce restaurant !");
    println("3. Faire une évaluation complète de ce restaurant !");
    println("4. Editer ce restaurant");
    println("5. Editer l'adresse du restaurant");
    println("6. Supprimer ce restaurant");
    println("0. Revenir au menu principal");
  }

  private void proceedRestaurantMenu(int choice, Restaurant restaurant) {
    switch (choice) {
      case 1:
        addBasicEvaluation(restaurant, true);
        break;
      case 2:
        addBasicEvaluation(restaurant, false);
        break;
      case 3:
        evaluateRestaurant(restaurant);
        break;
      case 4:
        editRestaurant(restaurant);
        break;
      case 5:
        editRestaurantAddress(restaurant);
        break;
      case 6:
        deleteRestaurant(restaurant);
        break;
      case 0:
      default:
        break;
    }
  }
  //Pas modifié encore
  private void addBasicEvaluation(Restaurant restaurant, Boolean like) {
    println("Aimez-vous ce restaurant ? (oui/non)");
    String likeInput = readString();
    boolean likeRestaurant = likeInput.trim().equalsIgnoreCase("oui");

    BasicEvaluation eval = new BasicEvaluation(null, LocalDate.now(), restaurant, likeRestaurant, getIpAddress());
    restaurant.getEvaluations().add(eval);
    basicEvaluationService.saveOrUpdate(eval);
    println("Votre vote a été pris en compte !");
  }

  private String getIpAddress() {
    try {
      return Inet4Address.getLocalHost().toString();
    } catch (UnknownHostException ex) {
      throw new RuntimeException(ex);
    }
  }

  private void evaluateRestaurant(Restaurant restaurant) {
    println("Merci d'évaluer ce restaurant !");
    println("Quel est votre nom d'utilisateur ? ");
    String username = readString();
    println("Quel commentaire aimeriez-vous publier ?");
    String comment = readString();

    EvaluationCriteriaService evaluationCriteriaService = new EvaluationCriteriaService();
    Set<EvaluationCriteria> evaluationCriterias = new HashSet<>(evaluationCriteriaService.findAll());

    CompleteEvaluation eval = new CompleteEvaluation(null, LocalDate.now(), restaurant, comment,
        username);
    restaurant.getEvaluations().add(eval);

    println("Veuillez svp donner une note entre 1 et 5 pour chacun de ces critères : ");

    evaluationCriterias.forEach(currentCriteria -> {
      println(currentCriteria.getName() + " : " + currentCriteria.getDescription());
      Integer note = readInt();
      Grade grade = new Grade(null, note, eval, currentCriteria);
      eval.getGrades().add(grade);
    });
    completeEvaluationService.saveOrUpdate(eval);

    println("Votre évaluation a bien été enregistrée, merci !");
  }

  private void editRestaurant(Restaurant restaurant) {
    println("Edition d'un restaurant !");

    println("Nouveau nom : ");
    restaurant.setName(readString());
    println("Nouvelle description : ");
    restaurant.setDescription(readString());
    println("Nouveau site web : ");
    restaurant.setWebsite(readString());
    println("Nouveau type de restaurant : ");

    RestaurantTypeService restaurantTypeService = new RestaurantTypeService();
    Set<RestaurantType> restaurantTypes = new HashSet<>(restaurantTypeService.findAll());

    RestaurantType newType = pickRestaurantType(restaurantTypes);
    if (newType != null && !newType.equals(restaurant.getType())) {
      restaurant.setType(newType);
    }

    RestaurantService restaurantService = new RestaurantService();
    restaurantService.saveOrUpdate(restaurant);

    println("Merci, le restaurant a bien été modifié !");
  }

  private void editRestaurantAddress(Restaurant restaurant) {
    println("Edition de l'adresse d'un restaurant !");

    println("Nouvelle rue : ");
    restaurant.getAddress().setStreet(readString());

    CityService cityService = new CityService();
    Set<City> cities = new HashSet<>(cityService.findAll());

    City newCity = pickCity(cities);
    if (newCity != null && !newCity.equals(restaurant.getAddress().getCity())) {
      restaurant.getAddress().setCity(newCity);
    }

    RestaurantService restaurantService = new RestaurantService();
    restaurantService.saveOrUpdate(restaurant);

    println("L'adresse a bien été modifiée ! Merci !");
  }

  private void deleteRestaurant(Restaurant restaurant) {
    println("Etes-vous sûr de vouloir supprimer ce restaurant ? (O/n)");
    String choice = readString();
    if ("o".equalsIgnoreCase(choice)) {
      RestaurantService restaurantService = new RestaurantService();
      restaurantService.delete(restaurant);
      println("Le restaurant a bien été supprimé !");
    }
  }

  private Optional<Restaurant> searchRestaurantByName(Set<Restaurant> restaurants,
      String name) {
    return restaurants.stream()
        .filter(current -> current.getName().equalsIgnoreCase(name.toUpperCase()))
        .findFirst();
  }

  private Optional<City> searchCityByZipCode(Set<City> cities, String zipCode) {
    return cities.stream()
        .filter(current -> current.getZipCode().equalsIgnoreCase(zipCode.toUpperCase()))
        .findFirst();
  }

  private Optional<RestaurantType> searchTypeByLabel(Set<RestaurantType> types, String label) {
    return types.stream()
        .filter(current -> current.getLabel().equalsIgnoreCase(label.toUpperCase()))
        .findFirst();
  }

  private int readInt() {
    int i = 0;
    boolean success = false;
    do
    { // Tant que l'utilisateur n'aura pas saisi un nombre entier, on va lui demander une nouvelle saisie
      try {
        i = scanner.nextInt();
        success = true;
      } catch (InputMismatchException e) {
        println("Erreur ! Veuillez entrer un nombre entier s'il vous plaît !");
      } finally {
        scanner.nextLine();
      }

    } while (!success);

    return i;
  }

  private String readString() {
    return scanner.nextLine();
  }

  private void println(String text) {
    printStream.println(text);
  }
}