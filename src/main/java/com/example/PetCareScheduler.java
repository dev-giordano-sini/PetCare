package com.example;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


public class PetCareScheduler {
    private final Scanner scanner = new Scanner(System.in);
    private List<PetCareServiceType> actionTypes = new ArrayList<>(Arrays.asList(PetCareServiceType.values()));
    private HashMap<String, Pet> pets = new HashMap<>();
    private static final String FILENAME = "pets.txt";
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PetCareScheduler() {
        loadData();
    }

    private void loadData() {
        URL resource = getClass().getClassLoader().getResource(FILENAME);
        assert resource != null;
        File myObj = new File(resource.getFile());

        // try-with-resources: Scanner will be closed automatically
        try (Scanner myReader = new Scanner(myObj)) {
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
    }

    public void run() {
        boolean exit = false;
        do {
            showActions();
            PetCareServiceType actionTypeSelected =  getActionType();
            exit = performAction(actionTypeSelected);
        } while(!exit);
    }

    private void showActions() {
        System.out.println("Make a choice from:");
        for (int counter = 0; counter < actionTypes.size(); counter++) {
            System.out.println(counter + ". " + actionTypes.get(counter).getDescription());
        }

        System.out.print("Select an action: ");
    }

    private PetCareServiceType getActionType() {
        boolean isValidAction = false;
        int actionID = -1;
        do {
            try {
                actionID = Integer.parseInt(scanner.nextLine());

                if (actionID >= 0 && actionID < actionTypes.size()) {
                    isValidAction = true;
                } else {
                    System.out.println("Invalid input. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Must insert a number");
            }
        } while (!isValidAction);

        return actionTypes.get(actionID);
    }

    private boolean performAction(PetCareServiceType actionTypeSelected) {
        boolean exit = false;
        switch (actionTypeSelected) {
            case REGISTER_A_PET->{
                registerPet();
            }
            case SCHEDULE_AN_APPOINTMENT->{
                scheduleAppointment();
            }
            case DISPLAY_DETAILS_OF_PETS_OR_APPOINTMENTS-> {
                displayDetails();
            }
            case STORE_THE_DETAILS_IN_A_FILE -> {
                storeData();
            }
            case GENERATE_REPORTS -> {
                generateReports();
            }
            case EXIT-> {
                System.out.println("==Exit action==");
                exit = true;
            }
            default -> {
                System.out.println("Invalid action");
            }
        }

        return exit;
    }

    private void registerPet() {
        System.out.println("==Register Pet action==");
        Scanner scanner = new Scanner(System.in);
        String ID;
        boolean idCheck = false;
        do {
            System.out.println("Insert Pet UNIQUE ID:");
            ID = scanner.nextLine();
            if (pets.containsKey(ID)) {
                System.out.println("Pet already exists");
            } else {
                idCheck = true;
            }
        } while (!idCheck);

        System.out.println("Insert Pet name:");
        String name = scanner.nextLine();

        System.out.println("Insert Pet breed:");
        String breed = scanner.nextLine();

        System.out.println("Insert Pet owner name:");
        String ownerName = scanner.nextLine();

        System.out.println("Insert Pet contact info:");
        String contactInfo = scanner.nextLine();

        System.out.println("Insert Pet registration date time in format dd/MM/yyyy:");
        LocalDate registrationDate = null;
        boolean dateOk = false;
        do {
            try {
                String registrationDateStr = scanner.nextLine();
                registrationDate = LocalDate.parse(registrationDateStr, dateFormatter);
                dateOk = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format");
            }
        } while (!dateOk);


        Pet pet = new Pet(ID, name, breed, ownerName, contactInfo, registrationDate);
        pets.put(ID, pet);

        System.out.println("** Pet has been registered successfully");
    }

    private void scheduleAppointment() {
        System.out.println("==Schedule Appointment Pet Action==");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Insert Pet ID:");
        String petID;
        boolean petIDExists = false;
        do {
            petID = scanner.nextLine();
            if (pets.containsKey(petID))
                petIDExists = true;
            else {
                System.out.println("Invalid Pet ID");
            }
        } while (!petIDExists);

        List<AppointmentType> appointmentTypes = new ArrayList<>(Arrays.asList(AppointmentType.values()));
        AppointmentType appointmentType = null;
        int appointmentTypeIndex = -1;
        boolean isValidAppointment = false;
        int appointmentTypeSize = appointmentTypes.size();
        do {
            for (int counter = 0; counter < appointmentTypeSize; counter++) {
                System.out.println(counter + ". " + appointmentTypes.get(counter).getDescrption());
            }
            try {
                System.out.print("Select an appointment type: ");
                appointmentTypeIndex = Integer.parseInt(scanner.nextLine());

                if (appointmentTypeIndex >= 0 && appointmentTypeIndex < appointmentTypeSize) {
                    isValidAppointment = true;
                } else {
                    System.out.println("Invalid input. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Must insert a number");
            }

            System.out.print("Select an action: ");

        } while (!isValidAppointment);

        appointmentType = appointmentTypes.get(appointmentTypeIndex);

        LocalDate appointmentDate = null;
        System.out.println("Select date in format yyyy-MM-dd: ");
        boolean dateOk = false;
        do {
            try {
                String appointmentDateString = scanner.nextLine();
                appointmentDate = LocalDate.parse(appointmentDateString, dateFormatter);
                dateOk = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format");
            }
        } while (!dateOk);

        boolean timeOk = false;
        LocalTime appointmentTime = null;
        do {
            try {
                String timeAppointmentStr = scanner.nextLine();
                appointmentTime = LocalTime.parse(timeAppointmentStr, timeFormatter);
                timeOk = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format");
            }
        } while (!timeOk);

        System.out.println("Insert optional notes: ");
        String notes = scanner.nextLine();

        Appointment appointment = new Appointment(appointmentType, appointmentDate, appointmentTime, notes);
        Pet pet = pets.get(petID);
        pet.setAppointment(appointment);
    }

    private void displayDetails() {
        System.out.println("== Display details of pets and/or appointments==");

        List<PetStoreInfoAction>  petStoreInfoActions = new ArrayList<>(Arrays.asList(PetStoreInfoAction.values()));
        PetStoreInfoAction petStoreInfoAction = null;
        boolean exit = false;
        int actionIndex = -1;
        do {
            System.out.println("Select an action: ");
            for (int counter = 0; counter < petStoreInfoActions.size(); counter++) {
                System.out.println(counter + ". " + petStoreInfoActions.get(counter).getDescription());
            }
            boolean isValidAction = false;
            do {
                try {
                    actionIndex = Integer.parseInt(scanner.nextLine());
                    if(actionIndex >= 0 &&  actionIndex < petStoreInfoActions.size()) {
                        isValidAction = true;
                    }
                    else {
                        System.out.println("Invalid input. Please try again.");
                    }
                }
                catch (NumberFormatException e) {
                    System.out.println("Must insert a number");
                }
            } while(!isValidAction);

            petStoreInfoAction = petStoreInfoActions.get(actionIndex);

            switch (petStoreInfoAction) {
                case DISPLAY_ALL_PETS -> {
                   showPets();
                }
                case DISPLAY_PAST_APPONINTMETS -> {
                    showAppointmentsHistory();
                }
                case DISPLAY_ALL_APPOINTMENTS_SPECIFIC_PET -> {
                    showAppointmentsByPet();
                }
                case DISPLAY_UPCOMING_ALL_APPOINTMENTS -> {
                    showNextAppointments();
                }
                case EXIT -> {
                    System.out.println("==Exit action==");
                    exit = true;
                }
                default -> {
                    System.out.println("Invalid input. Please try again.");
                }
            }

        } while (!exit);

    }

    private void showPets() {
        System.out.println("==Registered Pets==");
        pets.forEach((key, value) -> {
            System.out.println("Pet details: " + value.toString() + "\n");
        });
    }

    private void showAppointmentsByPet() {
        System.out.println("==Show appointments for a specific pet==");
        boolean isValidPetId = false;
        do {
            String petID = scanner.nextLine();

            if (pets.containsKey(petID)) {
                Pet pet = pets.get(petID);
                System.out.println(pet.getAppointments().toString());
                isValidPetId = true;
            }
            else {
                System.out.println("Invalid Pet ID");
            }

        } while(!isValidPetId);
    }

    private void showNextAppointments() {
        System.out.println("==Upcoming appointments for all pets==");
        LocalDate today = LocalDate.now();
        pets.forEach((petID, pet) -> {
           System.out.println("Pet : " + pet.getShortInfo());
           pet.getAppointments().stream().filter(appointment -> appointment.getDate().isAfter(today)).forEach(appointment -> {System.out.println(pet.getAppointments().toString());});
        });
    }

    private void showAppointmentsHistory() {
        System.out.println("==Past appointment history for each pet==");
        LocalDate today = LocalDate.now();
        pets.forEach((petID, pet) -> {
            System.out.println("Pet : " + pet.getShortInfo());
            pet.getAppointments().stream().filter(appointment -> appointment.getDate().isBefore(today)).forEach(appointment -> {System.out.println(pet.getAppointments().toString());});
        });
    }

    private void storeData() {
        System.out.println("==Store the details in a file==");
        try {
            FileWriter fileWriter = new FileWriter(FILENAME);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            pets.forEach((petID, pet) -> {
                try {
                    bufferedWriter.write(pet.toString());
                    bufferedWriter.newLine();
                } catch (IOException e) {
                    System.out.println("Error writing to the file: "  + e.getMessage());
                }
            });

            //flush and closing objects
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.flush();
            fileWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("Error writing to the file: " + e.getMessage());
        }
    }

    private void generateReports() {
        System.out.println("==Generate Reports action==");


    }


}
