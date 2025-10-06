package baseid.basearti.handlers;
import com.sap.cds.Result;
import com.sap.cds.Row;
import com.sap.cds.services.EventContext;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.Before;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.services.cds.CqnService;
import com.sap.cds.ql.Select;
import com.sap.cds.services.handler.annotations.ServiceName;
import cds.gen.travelservice.Employees;
import cds.gen.travelservice.Employees_;
import java.util.List;
import org.springframework.stereotype.Component;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.persistence.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.sap.cds.services.cds.CdsCreateEventContext;
import com.sap.cds.services.cds.CdsReadEventContext;
import java.util.Map;
import cds.gen.travelservice.TravelRequests_;
import cds.gen.travelservice.TravelRequests;
import cds.gen.travel.TravelRequest_;
import cds.gen.travel.TravelRequest;
//import com.sap.cds.services.handler.exceptions.CdsServiceException;


@Component
@ServiceName("TravelService")
public class TravelServiceHandler implements EventHandler{
    @Autowired
    PersistenceService db;

//    @On(event = CqnService.EVENT_READ, entity = Employees_.CDS_NAME)
//    public void onGetStudents(CdsReadEventContext context){
//        CqnSelect s = Select.from("travel.Employee")
//                .columns("ID","name","email","department");
//
//        Object result = db.run(s);
//        System.out.println(result);
//        context.setCqn(s);
//        Map<String,Object> m = new HashMap<>();
//        m.put("Message","Read Complete");
//        m.put("email","sample");
//        List<Employees> studentsList = db.run(s).listOf(Employees.class);
//        System.out.println(studentsList);
//        List<Map<String,Object>> message = new java.util.ArrayList<>(studentsList.stream().map(i -> {
//            Map<String, Object> row = new HashMap<>();
//            row.put("ID",i.getId());
//            row.put("name", i.getName());
//            row.put("email", i.getEmail());
//            row.put("department",i.getDepartment());
//            return row;
//        }).toList());
//        message.add(m);
//        context.setResult(message);
//        System.out.println(message);
//        System.out.println(context.);
//    }

    @Before(event=CqnService.EVENT_CREATE, entity=Employees_.CDS_NAME)
    public void onCreateEmployee(CdsCreateEventContext context, Employees employee){
        boolean hasError = false;
        if(employee.getName()==null||employee.getName().equals("")){
            context.getMessages().error("Please provide a Name").target("name").code("Name_Missing");
        }
        if(employee.getEmail()==null){
            context.getMessages().error("Please provide an Email").target("email").code("Email_Missing");
        }
        if(employee.getDepartment()==null){
            context.getMessages().error("Please provide a Department").target("department").code("Department_Missing");
        }
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (employee.getEmail()!=null) {
            Pattern pattern = Pattern.compile(emailPattern);
            Matcher matcher = pattern.matcher(employee.getEmail());
            if (!matcher.matches()) {
                context.getMessages().error("Please provide a valid Email").target("email").code("Invalid_Email");

            }
        }
    }
    @Before(event=CqnService.EVENT_CREATE, entity=TravelRequests_.CDS_NAME)
    public void onCreateRequest(CdsCreateEventContext context, TravelRequests travelRequest){
        if(travelRequest.getDestination()==null){
            context.getMessages().error("Please Enter the destination").target("destination").code("Destination_Missing");
        }
        if(travelRequest.getStartDate().isAfter(travelRequest.getEndDate())||travelRequest.getStartDate().isEqual(travelRequest.getEndDate())){
            context.getMessages().error("Please Enter a valid Start and End date").target("endDate").code("Invalid_Dates");
        }
        String empId = (String) travelRequest.get("employee_ID");
        System.out.println(empId);
        CqnSelect select = Select.from(TravelRequest_.class)
                .where(t -> t.employee_ID().eq(empId)
                        .and(t.status().eq("Submitted"))
                        .and(t.startDate().le(travelRequest.getEndDate()))
                        .and(t.endDate().ge(travelRequest.getStartDate())));

        System.out.println(select);
        List<TravelRequests> existing = db.run(select).listOf(TravelRequests.class);
        if(!existing.isEmpty()){
            context.getMessages().error("Overlapping travel request!").target("startDate").code("Overlap_Error");
            throw new RuntimeException("Overlapping travel request!");
        }
    }
}
