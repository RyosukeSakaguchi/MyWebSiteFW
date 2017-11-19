/**
 *
 */

var count;
function BoxChecked(check) {
	for (count = 0; count < document.form1["|delListId[${seq}]|"].length; count++) {
		document.form1["|delListId[${seq}]|"][count].checked = check; // チェックボックスをON/OFFにする
	}
}