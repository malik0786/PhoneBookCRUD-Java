package com.phonebook.main;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Scanner;

public class PhoneBook {

    public static String help = " H  Help ***  A  Add contact  ***  S  Search  ***  D  DELETE  ***  L  List All Entries  ***  U UPDATE RECORDS -  Q Exit :";

    public static void main(String[] args) {
        try {
            Connection con = null;
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to a selected database...");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/PhoneBook", "root", "root");
            System.out.println("Connected database successfully...");
            Statement statement = con.createStatement();
            String sql = "create table IF NOT EXISTS contacts (" +
                    "id int auto_increment primary key," +
                    "name varchar(45)," +
                    "lname varchar(45)," +
                    "phone varchar(45) UNIQUE," +
                    "CONSTRAINT name UNIQUE (name,phone))";
            statement.executeUpdate(sql);
            System.out.println("Created table in given database...");
            System.out.println("\n\n---- Functions of PhoneBook ----\n\n");
            Scanner s = new Scanner(System.in);
            String command = "";
            for (; ; ) {
                System.out.print("MAIN MENU " + help + "\n:");
                command = s.next().trim();
                if (command.trim().isEmpty()) {
                    continue;
                }

                if (command.equalsIgnoreCase("H")) {
                    System.out.println(help);
                } else if (command.equalsIgnoreCase("A")) {
                    System.out.println("Type details in following format : First Name , Last Name, PhoneNumber\n");
                    for (; ; ) {
                        String data = s.next().trim();
                        String[] temp = data.split(",");
                        if (temp.length != 3) {
                            System.out.println("Wrong Insertion Format, Correct formart is : First Name,Last Name,PhoneNumber :");
                            continue;
                        }
                        statement.executeUpdate("insert into contacts (name,lname,phone) values" + "('" + temp[0] + "','" + temp[1] + "','" + temp[2] + "')");
                        System.out.println("Successfully Inserted into table contacts");
                        break;
                    }
                } else if (command.equalsIgnoreCase("Q")) {
                    con.close();
                    System.out.println("Exiting from PhoneBook");
                    System.exit(0);

                } else if (command.equalsIgnoreCase("S")) {
                    System.out.println("Enter the id or FirstName or LastName or PhoneNumber you're searching for");
                    String data = s.next().trim();
                    String query = "select * from contacts where id like '%" + data + "%' or name like '%" + data + "%' or lname like '%" + data + "%' or phone like '%" + data + "%'";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.clearParameters();
                    ResultSet rs = ps.executeQuery();
                    System.out.println("********          Results          *********");
                    System.out.println("ID\t\tName\t\tLast Name\t\tPhone");
                    while (rs.next()) {
                        System.out.println(rs.getString(1) + "\t\t" + rs.getString(2) + "\t\t" + rs.getString(3) + "\t\t" + rs.getString(4));
                    }

                } else if (command.equalsIgnoreCase("D")) {
                    System.out.println("Enter the ID need to deleted");
                    String dataDelete = s.next().trim();
                    String deleteQuery = "delete from contacts where id like '%" + dataDelete + "%'";
                    statement.executeUpdate(deleteQuery);
                    System.out.println("Record is successfully deleted from PhoneBook");

                } else if (command.equalsIgnoreCase("L")) {
                    String listQuery = "select * from contacts";
                    PreparedStatement ps = con.prepareStatement(listQuery);
                    ps.clearParameters();
                    ResultSet rs = ps.executeQuery();
                    System.out.println("********          Results          *********");
                    System.out.println("ID\t\tName\t\tLast Name\t\tPhone");
                    if (rs.next()) {
                        do {
                            System.out.println(rs.getString(1) + "\t\t" + rs.getString(2) + "\t\t" + rs.getString(3) + "\t\t" + rs.getString(4));
                        } while (rs.next());
                    } else {
                        System.out.println("No record Found in PhoneBook");
                    }


                } else if (command.equalsIgnoreCase("U")) {
                    System.out.println("Enter records to be updates");
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("Enter the ID need to be updated");
                    int id = Integer.parseInt(br.readLine());
                    System.out.println("Enter new fname");
                    String name = br.readLine();
                    System.out.println("Enter new last name");
                    String lname = br.readLine();
                    System.out.println("Enter new PhoneNumber");
                    String phone = br.readLine();
                    String sqlupdate = "update contacts set name=?, lname=? ,phone=? where id=?";
                    PreparedStatement p = con.prepareStatement(sqlupdate);
                    p.setString(1, name);
                    p.setString(2, lname);
                    p.setString(3, phone);
                    p.setInt(4, id);
                    p.executeUpdate();

                    System.out.println("Record update");
                } else {
                    System.out.println("Unknow command try again " + command);
                }
            }


        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Duplicate PhoneNumber or name or file");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
