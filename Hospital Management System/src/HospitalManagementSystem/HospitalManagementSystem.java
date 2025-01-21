package HospitalManagementSystem;

import javax.print.Doc;
import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {

    private static final String url="jdbc:mysql://localhost:3306/hospital10";
   private static final String username="root";

   private static final String password="12345";
    private static  Scanner scanner;
    private static  Connection connection;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, username, password);
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);

            while (true) {
                System.out.println("Hospital Management System ");
                System.out.println("1. Add Patients");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");

                System.out.println("Enter your Choice: ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        // Add patients
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        //View Patients
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        //View Doctors
                        doctor.viewDoctor();
                        System.out.println();
                        break;
                    case 4:
                        //Book Appointment
                        bookAppointment(patient,doctor,connection,scanner);
                        System.out.println();
                        break;
                    case 5:
                        System.out.println("Thank you for using Hospital Management System!!!");
                        return;
                    default:
                        System.out.println("Enter Valid Choice!!!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }




    }
    public  static  void bookAppointment(Patient patient, Doctor doctor,Connection connection,Scanner scanner){
        System.out.println("Enter Patient id: ");
        int patientId=scanner.nextInt();
        System.out.println("Enter Doctor Id: ");
        int doctorId=scanner.nextInt();
        System.out.println("Enter AppointmentDate(YYYY-MM-DD)");
        String appointmentDate=scanner.next();

        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
             if(checkDoctorAvailability(doctorId,appointmentDate,connection)){
                 String appointmentQuery="insert into appointments(patient_id, doctor_id, appointment_date) values(?,?,?)";
                 try{
                     PreparedStatement preparedStatement =connection.prepareStatement(appointmentQuery);
                     preparedStatement.setInt(1,patientId);
                     preparedStatement.setInt(2,doctorId);
                     preparedStatement.setString(3,appointmentDate);

                     int rowAffected=preparedStatement.executeUpdate();
                     if(rowAffected>0){
                         System.out.println("Appointment Booked!!");
                     }else{
                         System.out.println("Failed to book appointment!!");
                     }
                 }catch (SQLException e){
                     e.printStackTrace();
                 }
             }
        }else{
            System.out.println("Either Patient or doctor doesn't exist!!!");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection){
       String query="select count(*) from appointments where doctor_id = ? AND appointment_date = ?";
       try{
           PreparedStatement preparedStatement=connection.prepareStatement(query);
           preparedStatement.setInt(1,doctorId);
           preparedStatement.setString(2,appointmentDate);
           ResultSet resultSet =preparedStatement.executeQuery();
           if(resultSet.next()){
               int count=resultSet.getInt(1);
               if(count==0){
                   return true;
               }else{
                   return false;
               }
           }
       }catch (SQLException e){
           e.printStackTrace();
       }
       return false;
    }
}
