import scala.slick.driver.H2Driver.simple._

// The main application
object MyFirstSlick extends App {

  // The query interface for the Employees table
  val employees: TableQuery[Employees] = TableQuery[Employees]
  
  // The query interface for the department table
  val department: TableQuery[Department] = TableQuery[Department]
  
  // Create a connection (called a "session") to an in-memory H2 database
  val db = Database.forURL("jdbc:h2:mem:hello", driver = "org.h2.Driver")
  db.withSession { implicit session =>

    // Create the schema by combining the DDLs for the Employees and Departments    // tables using the query interfaces
    (employees.ddl ++ department.ddl).create
  
    /* Create / Insert */

    //Insert Employees
    employees += (101, "Bill Gates", "Seattle ,USA", "20 /04/1960",9999.99,"M")
    employees += (102, "Jhon Snow", "Brussels, Belgium", "01/01/1980",8999.99,"R")
    employees += (103, "Karan", "Argentina", "18/12/1990",7999.99,"K")
    employees += (104, "Nishit", "Gainesville ,USA", "19/08/1910",6999.99,"A")
    employees += (105, "Raghove", "Greece", "28/09/1972",198.23,"M")
    employees += (106, "Pri", "Italy", "28/01/1984",130.12,"M")
    employees += (107, "Shyam", "Netherlands", "28/04/1987",200.4,"M")
    employees += (108, "Kanika", "Germany", "28/02/1969",970.9,"M")
    employees += (109, "Abhishek", "England", "28/09/1972",598.23,"R")
    employees += (110, "Sakshi", "Spain", "28/01/1984",330.12,"R")
    employees += (111, "Harshal", "Portugal", "28/04/1987",230.4,"R")
    employees += (112, "Abhinav", "Costa Rica", "28/02/1969",570.9,"R")
    employees += (113, "Harshit", "Australia", "28/09/1972",1198.23,"K")
    employees += (114, "Apeksha", "China", "28/01/1984",1302.12,"K")
    employees += (115, "Tanvi", "Hong Kong", "28/04/1987",1200.4,"K")
    employees += (116, "Aditya", " Texas ,USA", "28/02/1969",970.9,"K")
    employees += (117, "Hero", "Mumbai", "28/09/1972",1928.23,"A")
    employees += (118, "Heroine", "Mumbai,India", "28/01/1984",1130.12,"A")
    employees += (119, "Saurabh", "Hyderabad, India", "28/04/1987",1200.4,"A")
    employees += (120, "Varun ", "Nagpur, India", "28/02/1969",1970.9,"A")
    employees += (119, "Harsh", "Columbia", "28/04/1987",12.4,"A")
    employees += (120, "Udit ", "Bangalore, India", "28/02/1969",70.9,"A")
    
    // Insert some department (using JDBC's batch insert feature)
    val departmentInsertResult: Option[Int] = department ++= Seq (
      ("M","Management",101),
      ("K","Kitchen",103),
      ("R","Receptionist",102),
      ("A","Accounts",104)
    )

    val allEmployees: List[(Int, String, String, String, Double, String)] =
      employees.list

    
    // Print the number of rows inserted
    departmentInsertResult foreach { numRows =>
      println(s"Inserted $numRows rows into the Department table")
    }
  
    /* Read / Query / Select */
    println("************************************************")
    /* Print the SQL for the Department and Employee query*/
    println("Generated SQL for base Departments query:\n" + department.selectStatement)

    println("Generated SQL for base Employees query:\n" + employees.selectStatement)
    println("************************************************")   
    /* Query the Department table using a foreach and print each row */
    department foreach { case (deptid, deptname, manager) =>
      println("  " + deptid + "\t" + deptname + "\t" + manager )
    }
    println("************************************************")
     /* Query the Employee table using a foreach and print each row */
    employees foreach { case (empid, name, address, dob, salary, department) =>
      println("  " + empid + "\t" + name + "\t" + address + "\t" + dob+ "\t" + salary+ "\t" + department )
    }

  /* Filtering / Where */
    
    println("************************************************")
     // Construct a query where the salary of Employee is > 2000.0
    val filterQuery: Query[Employees, (Int, String, String, String, Double, String), Seq] =
      employees.filter(_.salary > 2000.0)

    println("Generated SQL for filter query:\n" + filterQuery.selectStatement)

    // Execute the query
    println(filterQuery.list)
    
    /* Delete */
    println("************************************************")
    // Construct a delete query that deletes employees with a salary less than 100.0
    val deleteQuery: Query[Employees, (Int, String, String, String, Double, String), Seq ]=
      employees.filter(_.salary < 100.0)

    // Print the SQL for the Employees delete query
    println("Generated SQL for Employees delete:\n" + deleteQuery.deleteStatement)

    // Perform the delete
    val numDeletedRows = deleteQuery.delete

    println(s"Deleted $numDeletedRows rows")
  
  
    /* Selecting Specific Columns */
  
    println("************************************************") 
    // Construct a new Employee query that just selects the name
    val justNameQuery: Query[Column[String], String, Seq] = employees.map(_.name)
  
    println("Generated SQL for query returning just the name:\n" +
      justNameQuery.selectStatement)
  
    // Execute the query
    println(justNameQuery.list)
  
  
    /* Sorting / Order By */
  
     println("************************************************")
    
    val sortBySalaryQuery: Query[Employees, (Int, String, String, String, Double, String), Seq] =
      employees.sortBy(_.salary)
  
    println("Generated SQL for query sorted by price:\n" +
      sortBySalaryQuery.selectStatement)
  
    // Execute the query
    println(sortBySalaryQuery.list)
  
    
    /* Query Composition */
  
    println("************************************************")
    val composedQuery: Query[Column[String], String, Seq] =
      employees.sortBy(_.name).take(5).filter(_.salary > 6000.0).map(_.name)
  
    println("Generated SQL for composed query:\n" +
      composedQuery.selectStatement)
  
    // Execute the composed query
    println(composedQuery.list)
  
    
    /* Joins */
  
    println("************************************************")
    // Join the tables using the relationship defined in the Departments table
    val joinQuery: Query[(Column[String], Column[String] , Column[Double]), (String, String, Double), Seq] = for {
      d <- department
      e <- d.employees
    } yield (d.deptname, e.name , e.salary)

    println("Generated SQL for the join query:\n" + joinQuery.selectStatement)

    // Print the rows which contain the department manager's name , department name and their salary
    println(joinQuery.list)
    
    
    /* Computed Values */
    
    println("************************************************")
    // Create a new computed column that calculates the max salary
    val maxSalaryColumn: Column[Option[Double]] = employees.map(_.salary).max
    
    println("Generated SQL for max salary column:\n" + maxSalaryColumn.selectStatement)
    
    // Execute the computed value query
    println(maxSalaryColumn.run)
    
    println("************************************************")
    // Create a new computed column that calculates the min salary
    val minSalaryColumn: Column[Option[Double]] = employees.map(_.salary).min
    
    println("Generated SQL for min salary column:\n" + minSalaryColumn.selectStatement)
    
    // Execute the computed value query
    println(minSalaryColumn.run)
    
    }
}
