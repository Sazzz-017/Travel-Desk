using travel as t from '../db/data-model';

service TravelService {
  entity Employees as projection on t.Employee;
  entity TravelRequests as projection on t.TravelRequest;
  entity Approvals as projection on t.Approval;

  action GetEmployeesCivil();
  action SubmitTravelRequest(ID: UUID);
  action ApproveRequest(ID: UUID, approverID: UUID, comments: String);
  action RejectRequest(ID: UUID, approverID: UUID, comments: String);
}