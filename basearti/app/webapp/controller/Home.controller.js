sap.ui.define(
    [
        "sap/ui/core/mvc/Controller"
    ],
    function(BaseController) {
      "use strict";

      return BaseController.extend("myprom.controller.Home", {
        onInit: function() {
        },
        onEmployeePress: function(){
          this.getOwnerComponent().getRouter().navTo("employeeCreate");
        },
        onManagerPress: function(){
          this.getOwnerComponent().getRouter().navTo("manager");
        }
      });
    }
  );