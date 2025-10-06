sap.ui.define([
    "sap/ui/core/mvc/Controller",
    "sap/m/MessageToast"
], function (Controller, MessageToast) {
    "use strict";

    return Controller.extend("myprom.controller.EmployeeCreate", {
        onInit: function () {
        },

        onSave: function () {
            const oView = this.getView();
            const oModel = oView.getModel();

            const payload = {
                name: oView.byId("inp1").getValue(),
                email: oView.byId("inp2").getValue(),
                department: oView.byId("inp3").getValue()
            };

            const oListBinding = oModel.bindList("/Employees");
            const oContext = oListBinding.create(payload);

            oContext.created()
                .then(() => {
                    const createdData = oContext.getObject();
                    const id = createdData.ID;
                    this.getOwnerComponent().getRouter().navTo("travelCreate", { employeeId: id });
                })
                .catch((oError) => {
                    MessageToast.show("Create failed");
                    console.error(oError);
                });
        }
    });
});