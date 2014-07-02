import scala.slick.driver.H2Driver.simple._
import scala.slick.lifted.{ProvenShape, ForeignKeyQuery}

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
