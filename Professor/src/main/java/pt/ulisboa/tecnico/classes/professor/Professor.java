package pt.ulisboa.tecnico.classes.professor;

import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer.OpenEnrollmentsRequest;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer.OpenEnrollmentsResponse;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer.*;
import pt.ulisboa.tecnico.classes.Stringify;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ResponseCode;

import java.util.Scanner;

public class Professor {

  private static final String EXIT_CMD = "exit";
  private static final String LIST_CMD = "list";
  private static final String OE_CMD = "openEnrollments";
  private static final String CE_CMD = "closeEnrollments";
  private static final String CAN_ENR_CMD = "cancelEnrollment";


  public static void main(String[] args) {

    final String host = "localhost";
    final int port = 5000;

    try (ProfessorFrontend frontend = new ProfessorFrontend(host, port); Scanner scanner = new Scanner(System.in)) {
      while (true) {
        System.out.printf("%n> ");
        try {
          String[] line = scanner.nextLine().split(" ");
          switch (line[0])
          {
            case EXIT_CMD:
              break;

            case LIST_CMD:
              ListClassRequest list_req = ListClassRequest.newBuilder().build();
              ListClassResponse list_res = frontend.setListClass(list_req);
              if (ResponseCode.forNumber(frontend.getCode(list_res)) == ResponseCode.OK)
                System.out.println(Stringify.format(frontend.getClassState(list_res)));
              else if (ResponseCode.forNumber(frontend.getCode(list_res)) == ResponseCode.INACTIVE_SERVER)
                System.out.println(Stringify.format(ResponseCode.INACTIVE_SERVER));
              break;

            case OE_CMD:
              int numStudents = Integer.parseInt(line[1]);
              OpenEnrollmentsRequest oe_req = OpenEnrollmentsRequest.newBuilder().setCapacity(numStudents).build();
              OpenEnrollmentsResponse oe_res = frontend.setOE(oe_req);
              if (ResponseCode.forNumber(frontend.getCodeOE(oe_res)) == ResponseCode.OK)
                System.out.println(Stringify.format(ResponseCode.OK));
              else if (ResponseCode.forNumber(frontend.getCodeOE(oe_res)) == ResponseCode.INACTIVE_SERVER)
                System.out.println(Stringify.format(ResponseCode.INACTIVE_SERVER));
              else if (ResponseCode.forNumber(frontend.getCodeOE(oe_res)) == ResponseCode.ENROLLMENTS_ALREADY_OPENED)
                System.out.println(Stringify.format(ResponseCode.ENROLLMENTS_ALREADY_OPENED));
              break;

            case CE_CMD:
              CloseEnrollmentsRequest ce_req = CloseEnrollmentsRequest.newBuilder().build();
              CloseEnrollmentsResponse ce_res = frontend.setCE(ce_req);
              if (ResponseCode.forNumber(frontend.getCodeCE(ce_res)) == ResponseCode.OK)
                System.out.println(Stringify.format(ResponseCode.OK));
              else if (ResponseCode.forNumber(frontend.getCodeCE(ce_res)) == ResponseCode.INACTIVE_SERVER)
                System.out.println(Stringify.format(ResponseCode.INACTIVE_SERVER));
              else if (ResponseCode.forNumber(frontend.getCodeCE(ce_res)) == ResponseCode.ENROLLMENTS_ALREADY_CLOSED)
                System.out.println(Stringify.format(ResponseCode.ENROLLMENTS_ALREADY_CLOSED));
              break;

            case CAN_ENR_CMD:

              if (!frontend.checkStudentId(line[1]))
                break;

              CancelEnrollmentRequest c_req = CancelEnrollmentRequest.newBuilder().setStudentId(line[1]).build();
              CancelEnrollmentResponse c_res = frontend.setCanEnr(c_req);
              if (ResponseCode.forNumber(frontend.getCodeCanEnr(c_res)) == ResponseCode.OK)
                System.out.println(Stringify.format(ResponseCode.OK));
              else if (ResponseCode.forNumber(frontend.getCodeCanEnr(c_res)) == ResponseCode.INACTIVE_SERVER)
                System.out.println(Stringify.format(ResponseCode.INACTIVE_SERVER));
              else if (ResponseCode.forNumber(frontend.getCodeCanEnr(c_res)) == ResponseCode.NON_EXISTING_STUDENT)
                System.out.println(Stringify.format(ResponseCode.NON_EXISTING_STUDENT));
              break;

            default:
              System.out.println(Stringify.format(ResponseCode.UNRECOGNIZED));
          }
        } catch (NullPointerException e) {
          System.out.println("Error: null pointer caught");
        }
        catch (NumberFormatException e) {
          System.out.println("Error: string given instead of a number");
        }
      }
    } finally {
      System.out.println("");
    }
  }

}
