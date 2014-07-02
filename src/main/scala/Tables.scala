import scala.slick.driver.H2Driver.simple._
import scala.slick.lifted.{ProvenShape, ForeignKeyQuery}
/*
// A Suppliers table with 6 columns: id, name, street, city, state, zip
class Suppliers(tag: Tag)
  extends Table[(Int, String, String, String, String, String)](tag, "SUPPLIERS") {

  // This is the primary key column:
  def id: Column[Int] = column[Int]("SUP_ID", O.PrimaryKey)
  def name: Column[String] = column[String]("SUP_NAME")
  def street: Column[String] = column[String]("STREET")
  def city: Column[String] = column[String]("CITY")
  def state: Column[String] = column[String]("STATE")
  def zip: Column[String] = column[String]("ZIP")
  
  // Every table needs a * projection with the same type as the table's type parameter
  def * : ProvenShape[(Int, String, String, String, String, String)] =
    (id, name, street, city, state, zip)
}

// A Coffees table with 5 columns: name, supplier id, price, sales, total
class Coffees(tag: Tag)
  extends Table[(String, Int, Double, Int, Int)](tag, "COFFEES") {

  def name: Column[String] = column[String]("COF_NAME", O.PrimaryKey)
  def supID: Column[Int] = column[Int]("SUP_ID")
  def price: Column[Double] = column[Double]("PRICE")
  def sales: Column[Int] = column[Int]("SALES")
  def total: Column[Int] = column[Int]("TOTAL")
  
  def * : ProvenShape[(String, Int, Double, Int, Int)] =
    (name, supID, price, sales, total)
  
  // A reified foreign key relation that can be navigated to create a join
  def supplier: ForeignKeyQuery[Suppliers, (Int, String, String, String, String, String)] = 
    foreignKey("SUP_FK", supID, TableQuery[Suppliers])(_.id)
}
*/
// An Employee Table with 6 columns EmpID, Name, Address, Date of Birth ,Salary, Department

class Employees( tag : Tag)
    extends Table[(Int,String,String,String,Double,String)](tag,"EMPLOYEES"){
       
  def empid: Column[Int] = column[Int]("EMPID", O.PrimaryKey)
  def name: Column[String] = column[String]("NAME")
  def address: Column[String] = column[String]("ADDRESS")
  def dob: Column[String] = column[String]("DOB")
  def salary: Column[Double] = column[Double]("SALARY")
  def department: Column[String] = column[String]("DEPARTMENT")
    
  def * : ProvenShape[(Int,String,String,String,Double,String)] =
    (empid, name, address, dob, salary, department)        

        
}

// A department table with 3 columns, DeptID , DeptName and Manager
class Department( tag : Tag)
    extends Table[(String, String , Int)](tag, "DEPARTMENT"){
    
    def deptid: Column[String] = column[String]("DEPTID", O.PrimaryKey)
    def deptname: Column[String] = column[String]("DEPTNAME")
    def manager: Column[Int] = column[Int]("MANAGER")
    
    def * : ProvenShape[(String,String,Int)] =
    (deptid, deptname, manager)
    
    // A foreign key relation to get all the details about the manager
    def employees: ForeignKeyQuery[Employees, (Int, String, String, String, Double, String)] = 
    foreignKey("EMP_FK", manager, TableQuery[Employees])(_.empid)
    }
