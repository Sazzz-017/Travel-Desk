sap.ui.define([
    "sap/ui/core/mvc/Controller",
    "sap/m/MessageToast",
    "sap/ui/model/Filter",
    "sap/ui/model/FilterOperator"
], function (Controller, MessageToast, Filter, FilterOperator) {
    "use strict";

    return Controller.extend("myprom.controller.TravelRequestCreate", {

        onInit: function () {
            // Attach route matched handler
            this.getOwnerComponent().getRouter()
                .getRoute("travelCreate")
                .attachMatched(this._onRouteMatched.bind(this));
        },

        _onRouteMatched: function (oEvent) {
            this._employeeId = oEvent.getParameter("arguments").employeeId;


            const oTable = this.byId("requestsTable");
            const oBinding = oTable.getBinding("items");
            if (oBinding) {
                oBinding.filter([new Filter("employee_ID", FilterOperator.EQ, this._employeeId)]);
            }
        },

        onSubmit: function () {
            const oView = this.getView();
            const oModel = oView.getModel();
            const oListBinding = oModel.bindList("/TravelRequests");
            const payload = {
                ID: (crypto && crypto.randomUUID) ? crypto.randomUUID() : Date.now().toString(),
                employee_ID: this._employeeId,
                destination: oView.byId("destination").getValue(),
                startDate: oView.byId("startDate").getDateValue().toISOString().split('T')[0],
                endDate: oView.byId("endDate").getDateValue().toISOString().split('T')[0],
                purpose: oView.byId("purpose").getValue(),
                status: "Submitted",
                createdAt: new Date().toISOString()
            };
            const oContext = oListBinding.create(payload);

            oContext.created()
                .then(() => {
                    MessageToast.show("Travel request submitted");
                    const oTable = this.byId("requestsTable");
                    const oBinding = oTable.getBinding("items");
                    if (oBinding) {
                        oBinding.filter([new Filter("employee_ID", FilterOperator.EQ, this._employeeId)]);
                        oBinding.refresh();
                    }
                    oView.byId("destination").setValue("");
                    oView.byId("startDate").setValue("");
                    oView.byId("endDate").setValue("");
                    oView.byId("purpose").setValue("");
                })
                .catch((err) => {
                    MessageToast.show("Submit failed");
                    console.error(err);
                });
        }
    });
});
