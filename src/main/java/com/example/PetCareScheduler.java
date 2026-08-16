package com.example;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


public class PetCareScheduler {
    private static final String PETS_FILENAME = "pets.txt";
    private static final String APPOINTMENT_FILENAME = "appointments.txt";
    private final Scanner scanner = new Scanner(System.in);
    private final JsonFileStorage storage = new JsonFileStorage();
    private final List<PetCareServiceType> actionTypes = new ArrayList<>(Arrays.asList(PetCareServiceType.values()));
    private final HashMap<String, Pet> pets = new HashMap<>();
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public PetCareScheduler() {
        loadData();
    }

    private void loadData() {
        try {
            storage.readList(PETS_FILENAME, Pet.class,
                            "id", "name", "breed", "ownerName", "contactInfo", "registrationDate")
                    .forEach(pet -> pets.put(pet.getID(), pet));
            storage.readList(APPOINTMENT_FILENAME, AppointmentWithID.class,
                            "id", "type", "date", "time", "note")
                    .forEach(this::addAppointmentToPet);
        } catch (IOException e) {
            System.err.println("Error reading JSON data: " + e.getMessage());
        }
    }

    private void addAppointmentToPet(AppointmentWithID appointment) {
        Pet pet = pets.get(appointment.getID());
        if (pet != null) {
            pet.setAppointment(appointment);
        } else {
            System.out.println("No pet found for the appointment with ID: " + appointment.getID());
        }
    }


    public void run() {
        boolean exit = false;
        do {
            showActions();
            PetCareServiceType actionTypeSelected = getActionType();
            exit = performAction(actionTypeSelected);
        } while (!exit);
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
            case REGISTER_A_PET -> {
                registerPet();
            }
            case SCHEDULE_AN_APPOINTMENT -> {
                scheduleAppointment();
            }
            case DISPLAY_DETAILS_OF_PETS_OR_APPOINTMENTS -> {
                displayDetails();
            }
            case STORE_THE_DETAILS_IN_A_FILE -> {
                storeData();
            }
            case GENERATE_REPORTS -> {
                generateReports();
            }
            case EXIT -> {
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

        System.out.println("Insert Pet registration date time in format yyyy-MM-dd:");
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
                System.out.println(counter + ". " + appointmentTypes.get(counter).getDescription());
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

        } while (!isValidAppointment);

        appointmentType = appointmentTypes.get(appointmentTypeIndex);

        LocalDate appointmentDate = null;
        System.out.println("Select date in format yyyy-MM-dd: ");
        boolean dateOk = false;
        LocalDate today = LocalDate.now();
        boolean isFutureDate = false;
        boolean isDateToday = false;
        do {
            try {
                String appointmentDateString = scanner.nextLine();
                appointmentDate = LocalDate.parse(appointmentDateString, dateFormatter);

                isDateToday = appointmentDate.isEqual(today);
                isFutureDate = appointmentDate.isAfter(today);
                if (isDateToday || isFutureDate) {
                    dateOk = true;
                } else {
                    System.out.println("Appointment date must be equal or greater than today");
                }

            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format");
            }
        } while (!dateOk);

        boolean timeOk = false;
        LocalTime appointmentTime = null;
        LocalTime todayTime = LocalTime.now();
        do {
            try {
                System.out.println("Select time in format HH:mm: ");
                String timeAppointmentStr = scanner.nextLine();
                appointmentTime = LocalTime.parse(timeAppointmentStr, timeFormatter);
                boolean isFutureTime = appointmentTime.isAfter(todayTime);

                if (isDateToday) {
                    if (isFutureTime) {
                        timeOk = true;
                    } else {
                        System.out.println("Appointment time must be greater than now");
                    }
                } else if (isFutureDate) {
                    timeOk = true;
                }

            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format");
            }
        } while (!timeOk);

        System.out.println("Insert optional notes: ");
        String notes = scanner.nextLine();

        Appointment appointment = new Appointment(appointmentType, appointmentDate, appointmentTime, notes);
        Pet pet = pets.get(petID);
        pet.setAppointment(appointment);

        System.out.println("**Appointment scheduled correctly for ped with ID " + petID);
    }

    private void displayDetails() {
        System.out.println("== Display details of pets and/or appointments==");

        List<PetStoreInfoAction> petStoreInfoActions = new ArrayList<>(Arrays.asList(PetStoreInfoAction.values()));
        PetStoreInfoAction petStoreInfoAction = null;
        boolean exit = false;
        int actionIndex = -1;
        do {
            System.out.println("Select an action from list:");
            for (int counter = 0; counter < petStoreInfoActions.size(); counter++) {
                System.out.println(counter + ". " + petStoreInfoActions.get(counter).getDescription());
            }
            boolean isValidAction = false;
            do {
                try {
                    System.out.print("Select an action: ");
                    actionIndex = Integer.parseInt(scanner.nextLine());
                    if (actionIndex >= 0 && actionIndex < petStoreInfoActions.size()) {
                        isValidAction = true;
                    } else {
                        System.out.println("Invalid input. Please try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Must insert a number");
                }
            } while (!isValidAction);

            petStoreInfoAction = petStoreInfoActions.get(actionIndex);

            switch (petStoreInfoAction) {
                case DISPLAY_ALL_PETS -> {
                    showPets();
                }
                case DISPLAY_PAST_APPOINTMENTS -> {
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
        if (pets.isEmpty()) {
            System.out.println("No pets registered");
        } else {
            pets.forEach((key, value) -> {
                System.out.println("Pet details: " + value.toString() + "\n");
            });
        }

    }

    private void showAppointmentsByPet() {
        System.out.println("==Show appointments for a specific pet==");
        boolean isValidPetId = false;
        do {
            System.out.print("Insert pet ID: ");
            String petID = scanner.nextLine();
            if (pets.containsKey(petID)) {
                Pet pet = pets.get(petID);
                if (pet.getAppointments().isEmpty()) {
                    System.out.println("Pet has no appointments");
                } else {
                    System.out.println(pet.getAppointments().toString());
                }

                isValidPetId = true;
            } else {
                System.out.println("Invalid Pet ID");
            }

        } while (!isValidPetId);
    }

    private void showNextAppointments() {
        System.out.println("==Upcoming appointments for all pets==");
        LocalDate today = LocalDate.now();
        pets.forEach((petID, pet) -> {
            System.out.println("Pet : " + pet.getShortInfo());
            pet.getAppointments().stream().filter(appointment -> appointment.getDate().isAfter(today)).forEach(appointment -> {
                System.out.println(appointment.getInfo());
            });
        });
    }

    private void showAppointmentsHistory() {
        System.out.println("==Past appointment history for each pet==");
        LocalDate today = LocalDate.now();
        pets.forEach((petID, pet) -> {
            System.out.println("Pet : " + pet.getShortInfo());
            pet.getAppointments().stream().filter(appointment -> appointment.getDate().isBefore(today)).forEach(appointment -> {
                System.out.println(appointment.getInfo());
            });
        });
    }

    private void storeData() {
        System.out.println("==Store the details in a file==");

        try {
            List<Pet> petsToStore = new ArrayList<>(pets.values());
            List<AppointmentWithID> appointmentsToStore = new ArrayList<>();
            pets.forEach((petID, pet) -> pet.getAppointments().forEach(appointment ->
                    appointmentsToStore.add(new AppointmentWithID(petID, appointment.getType(),
                            appointment.getDate(), appointment.getTime(), appointment.getNote()))));

            storage.writeListsAtomically(PETS_FILENAME, petsToStore,
                    APPOINTMENT_FILENAME, appointmentsToStore);
        } catch (IOException e) {
            System.out.println("Error writing JSON data: " + e.getMessage());
            return;
        }

        System.out.println("**Successfully wrote the files.");
    }

    private void generateReports() {
        System.out.println("==Generate Reports action==");

        printReportUpcomingAppointmentNextWeek();

        printReportPetsOverdueForVetVisit();
    }

    private void printReportUpcomingAppointmentNextWeek() {
        System.out.println("==Pets with upcoming appointments in the next week==");

        LocalDate today = LocalDate.now();
        final HashMap<String, List<Appointment>> appointmentHashMap = new HashMap<>();
        pets.forEach((petID, pet) -> {
            final int beginDayOfWeekDelta = Math.abs(today.getDayOfWeek().getValue() - DayOfWeek.FRIDAY.getValue());
            final LocalDate beginOfCurrentWeekDate = today.minusDays(beginDayOfWeekDelta);
            final LocalDate beginOfNextWeekDate = beginOfCurrentWeekDate.plusDays(7);
            final LocalDate endOfNextWeekDate = beginOfNextWeekDate.plusDays(6);
            pet.getAppointments().stream().filter(appointment -> appointment.getDate().isAfter(today)).forEach(appointment -> {

                int start = appointment.getDate().compareTo(beginOfCurrentWeekDate);
                int stop = appointment.getDate().compareTo(endOfNextWeekDate);

                boolean isInWeek = start >= 0 && stop <= 0;
                if (isInWeek) {
                    if(!appointmentHashMap.containsKey(petID)) {
                        appointmentHashMap.put(petID, new ArrayList<>());

                    }
                    appointmentHashMap.get(petID).add(appointment);
                }

            });
        });

        if(appointmentHashMap.isEmpty()) {
            System.out.println("There's no pets with upcoming appointments in the next week");
        }
        else {
            appointmentHashMap.forEach((petID, appointments) -> {
               System.out.println("Appointments list for Pet ID "  + petID + ":");
               appointments.forEach(appointment -> {
                   System.out.println(appointment.getInfo());
               });
               System.out.println("\n");
            });
        }
    }

    private void printReportPetsOverdueForVetVisit() {
        System.out.println("==Pets overdue for a vet visit (6 months)==");
        final LocalDate today = LocalDate.now();
        final LocalDate sixtMonthAgo =  today.minusMonths(6);
        final HashMap<String,HashSet<AppointmentType>> appointmentHashMap = new HashMap<>();
        pets.forEach((petID, pet) -> {
            pet.getAppointments().forEach(appointment -> {
                if(!appointment.getDate().isAfter(sixtMonthAgo)) {
                    if(!appointmentHashMap.containsKey(petID))
                        appointmentHashMap.put(petID, new HashSet<>());

                    appointmentHashMap.get(petID).add(appointment.getType());
                }
            });
        });


        if(appointmentHashMap.isEmpty()) {
            System.out.println("No pets overdue for a every type (vet visit, grooming, etc.)");
        }
        else {
            System.out.println("There are overdue for some pets");
            appointmentHashMap.forEach((petID, appointments) -> {
                System.out.println("Pet ID " + petID + ", types:");
                appointments.forEach(appointment -> {
                    System.out.println("* " + appointment.getDescription());
                });
                System.out.println();
            });
        }

    }

}
