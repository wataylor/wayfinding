/* Java Script utilities for the reporting system.*/

function Report() {
};

Report.selectorIDs = ["startSelectMonth", "startSelectDay", "startSelectYear",
"endSelectMonth", "endSelectDay", "endSelectYear"];

Report.clearReportFields = function () {
  UIU.resetManySelectionListIDs(Report.selectorIDs);
  UIU.hideMessageDivs();
  $("#report_content").empty();
};

Report.generateReport = function() {
  $.getJSON('../html.jsp?verb=5',
	$('#generateReportForm').serialize(),
	function(data) {
	  if (data['msg']) {
	    UIU.failure(data['msg']);
	  } else {
	    $("#report_content").empty();
	    $("#report_content").append(data.tb);
	    UIU.assignHTML("report_start", data.st);
	    UIU.assignHTML("report_end", data.et);
	    UIU.hideMessageDivs();
	    $("a#downloadReport").attr("href", data.fn);
	  }
	});
  return false;
};
