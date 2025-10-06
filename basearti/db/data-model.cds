namespace travel;

entity Employee {
  key ID       : UUID;
  name         : String(100);
  email        : String(200);
  department   : String(50);
}

entity TravelRequest {
  key ID        : UUID;
  employee      : Association to Employee;
  destination   : String(200);
  startDate     : Date;
  endDate       : Date;
  purpose       : String(500);
  status        : String(20);
  createdAt     : Timestamp;
}

entity Approval {
  key ID        : UUID;
  request       : Association to TravelRequest;
  approver      : Association to Employee;
  decision      : String(20);
  comments      : String(200);
  decisionDate  : Timestamp;
}
