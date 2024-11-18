import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;

abstract class Doctor {
    String name;
    String specialization;

    public Doctor(String name, String specialization) {
        this.name = name;
        this.specialization = specialization;
    }

    abstract String getProblem();
}

class Cardiologist extends Doctor {
    public Cardiologist(String name) {
        super(name, "Cardiology");
    }

    String getProblem() {
        return "Heart-related issues";
    }
}

class Dentist extends Doctor {
    public Dentist(String name) {
        super(name, "Dentistry");
    }

    String getProblem() {
        return "Dental issues";
    }
}

class Gynecologist extends Doctor {
    public Gynecologist(String name) {
        super(name, "Gynecology");
    }

    String getProblem() {
        return "Women's health issues";
    }
}

class Neurologist extends Doctor {
    public Neurologist(String name) {
        super(name, "Neurology");
    }

    String getProblem() {
        return "Neurological issues";
    }
}

interface Patient {
    void takeAppointment();
}

class PatientAppointment implements Patient {
    static ArrayList<PatientAppointment> appointments = new ArrayList<>();
    String name;
    int age;
    String bloodGroup;
    String phoneNumber;
    String chosenSlot;
    boolean requiresXRay;
    boolean requiresScanning;

    public PatientAppointment() {
        // Default constructor
    }

    public void takeAppointment() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n-----------------Health Care Hospital---------------\n");
        System.out.print("\nEnter patient name: ");
        name = scanner.nextLine();

        while (true) {
            try {
                System.out.print("Enter patient age: ");
                age = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid age.");
                scanner.next(); // Consume invalid input
            }
        }

        scanner.nextLine(); // Consume newline
        System.out.print("Enter patient blood group: ");
        bloodGroup = scanner.nextLine();

        while (true) {
            try {
                System.out.print("Enter patient phone number: ");
                phoneNumber = scanner.nextLine();
                if (isValidPhoneNumber(phoneNumber)) {
                    break;
                } else {
                    System.out.println("Invalid phone number. Please enter a valid 10-digit number.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid phone number.");
                scanner.next(); // Consume invalid input
            }
        }

        System.out.println("Available slots: 1. Morning-Heart Related Issues, 2. Afternoon-Dental Issues, 3. Evening-Women Related Issues");
        int slot = 0;
        while (true) {
            try {
                System.out.print("Choose a slot (1-3): ");
                slot = scanner.nextInt();
                if (slot >= 1 && slot <= 3 && isSlotAvailable(slot)) {
                    break; // Valid and available slot, exit the loop
                } else {
                    System.out.println("Invalid slot choice or slot is already full. Please choose again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Consume invalid input
            }
        }

        chosenSlot = getShift(slot);
        System.out.println("Appointment scheduled for " + chosenSlot);
        scanner.nextLine(); // Consume newline

        System.out.println("Do you want to take X-ray? (yes/no): ");
        requiresXRay = scanner.nextLine().equalsIgnoreCase("yes");

        System.out.println("Do you want to take Scanning? (yes/no): ");
        requiresScanning = scanner.nextLine().equalsIgnoreCase("yes");

        System.out.println("\n");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{10}");
    }

    private boolean isSlotAvailable(int slot) {
        int appointmentsPerSlot = 2;
        int appointmentsCount = 0;

        for (PatientAppointment appointment : appointments) {
            if (appointment.chosenSlot != null && appointment.chosenSlot.equals(getShift(slot))) {
                appointmentsCount++;
            }
        }
        return appointmentsCount < appointmentsPerSlot;
    }

    private String getShift(int slot) {
        switch (slot) {
            case 1:
                return "Morning";
            case 2:
                return "Afternoon";
            case 3:
                return "Evening";
            default:
                return "";
        }
    }

    public double calculateCharges() {
        double baseCharge = 500.0; // Base consultation charge
        double xRayCharge = requiresXRay ? 400.0 : 0.0;
        double scanningCharge = requiresScanning ? 500.0 : 0.0;
        return baseCharge + xRayCharge + scanningCharge;
    }
}

public class HospitalAppointmentSystem {
    public static void main(String[] args) {
        BufferedWriter writer;
        ArrayList<Doctor> doctors = new ArrayList<>();
        doctors.add(new Cardiologist("Dr. Ajay Bahadur"));
        doctors.add(new Dentist("Dr. Ritu Sharma"));
        doctors.add(new Gynecologist("Dr. Hrishikesh Pai"));
        doctors.add(new Neurologist("Dr. Aditya Gupta"));

        ArrayList<PatientAppointment> appointments = new ArrayList<>();
        for (Doctor doctor : doctors) {
            for (int i = 0; i < 2; i++) {
                PatientAppointment patient = new PatientAppointment();
                patient.takeAppointment();
                appointments.add(patient);
            }
        }

        try {
            writer = new BufferedWriter(new FileWriter("hospitalrecord.txt", true));
            for (PatientAppointment patient : appointments) {
                System.out.println("\nPatient Name: " + patient.name);
                writer.write("\nPatient Name: " + patient.name);

                System.out.println("Age: " + patient.age);
                writer.write("\nAge: " + patient.age);

                System.out.println("Blood Group: " + patient.bloodGroup);
                writer.write("\nBlood Group: " + patient.bloodGroup);

                System.out.println("Phone Number: " + patient.phoneNumber);
                writer.write("\nPhone Number: " + patient.phoneNumber);

                System.out.println("Chosen Slot: " + patient.chosenSlot);
                writer.write("\nChosen Slot: " + patient.chosenSlot);

                System.out.println("Doctor: " + getDoctorName(patient.chosenSlot));
                writer.write("\nDoctor: " + getDoctorName(patient.chosenSlot));

                System.out.println("Problem: " + getDoctorProblem(patient.chosenSlot));
                writer.write("\nProblem: " + getDoctorProblem(patient.chosenSlot));

                System.out.println("Total Charges: " + patient.calculateCharges() + " Rs/-");
                writer.write("\nTotal Charges: " + patient.calculateCharges() + " Rs/-\n");
            }
            writer.write("\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

    private static String getDoctorName(String chosenSlot) {
        switch (chosenSlot) {
            case "Morning":
                return "Dr. Ajay Bahadur";
            case "Afternoon":
                return "Dr. Ritu Sharma";
            case "Evening":
                return "Dr. Hrishikesh Pai";
            default:
                return "";
        }
    }

    private static String getDoctorProblem(String chosenSlot) {
        switch (chosenSlot) {
            case "Morning":
                return "Heart-related issues";
            case "Afternoon":
                return "Dental issues";
            case "Evening":
                return "Women's health issues";
            default:
                return "";
        }
    }
}
