package HospitalManagementSystem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;

    private Scanner scanner;

    public Patient(Connection connection,Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }

    public void addPatient(){
        System.out.print("Enter Patient Name: ");
        String name=scanner.next();
        System.out.println("Enter Patient Age: ");
        int age=scanner.nextInt();
        System.out.print("Enter Patient Gender: ");
        String gender=scanner.next();

        try {
            String query="INSERT INTO patients(name,age,gender) values(?,?,?)";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);

            int affectedRow=preparedStatement.executeUpdate();
            if(affectedRow>0){
                System.out.println("Patient added successfully!!");
            }else{
                System.out.println("Failed to add Patient!!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void viewPatients(){
        String query="select * from patients";
        try {
            PreparedStatement prepareStatement=connection.prepareStatement(query);
            ResultSet resultSet =prepareStatement.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+------------+--------------+------+--------+");
            System.out.println("| Patient id | Name         | Age  | Gender |");
            System.out.println("+------------+--------------+------+--------+");
            while (resultSet.next()){
                int id =resultSet.getInt("id");
                String name=resultSet.getString("name");
                int age =resultSet.getInt("Age");
                String gender=resultSet.getString("gender");
                System.out.printf("| %-10s | %-12s | %-4s | %-6s |\n",id,name,age,gender);
                System.out.println("+------------+--------------+------+--------+");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public boolean getPatientById(int id){
        String query="SELECT * FROM patients where id= ?";

        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
               return  true;
            }else{
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
             return false;
    }

}
