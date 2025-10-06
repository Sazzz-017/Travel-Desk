sap.ui.define([
    "sap/ui/core/mvc/Controller",
    "sap/m/MessageToast"
], function(Controller, MessageToast) {
    "use strict";

    return Controller.extend("myprom.controller.ManagerList", {

        onInit: function() {
            // Nothing special needed for initialization
        },

        onApprove: function(oEvt) {
            this._updateRequestStatus(oEvt, "Approved");
        },

        onReject: function(oEvt) {
            this._updateRequestStatus(oEvt, "Rejected");
        },

_updateRequestStatus: function(oEvt, sStatus) {
            const oButton = oEvt.getSource();
            const oContext = oButton.getParent().getBindingContext();
            const oModel = this.getView().getModel();
            oContext.setProperty("status", sStatus);

            MessageToast.show(`Request ${sStatus}`);
            const oRow = oButton.getParent();
            let oItem = oButton;
            while (oItem && !oItem.isA("sap.m.ColumnListItem")) {
                oItem = oItem.getParent();
            }
            if (oItem) {
                oItem.getCells().forEach(cell => {
                    if (cell.isA && cell.isA("sap.m.Button")) {
                        cell.setEnabled(false);
                    }
                });
            }
        }


    });
});
