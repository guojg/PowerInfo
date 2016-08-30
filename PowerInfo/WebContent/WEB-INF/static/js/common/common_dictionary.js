/*combobox的加载*****自定义传递参数*****/
function comboBoxInit(obj) {
	// url
	if (obj.url != null && obj.url != "") {
		url = obj.url;
	}
	var textkey = obj.textkey;
	var valuekey = obj.valuekey;

	$.ajax({
				url : url,
				type : 'post',
				dataType : 'json',
				async : false,
				success : function(data) {

					if (obj.multiple) {// 多选加上清空,全选
						if (typeof (obj.defaultVal) == "undefined") {// 默认值不传默认全选
							var option = {};
							option[valuekey] = "";
							option[textkey] = "<input type='checkbox' id='"
									+ obj.id
									+ "_qxf_checkbox' style='width:12px;' checked='checked'>全选";
							data.unshift(option);
							var comboboxObj = makeCombobox_common_dictionary(
									obj, data);
							// 如果是全选的默认把值都选上。
							var defaultValArr = new Array();
							for (var i = 0; i < data.length; i++) {
								if (data[i][valuekey] != "") {
									defaultValArr.push(data[i][valuekey] + '');
								}
							}
							comboboxObj.combobox("setValues", defaultValArr);

						} else if (obj.defaultVal == "") {// 默认值请选择,此时默认值是请选择
							var option = {};
							option.ID = "";
							option.TEXT = "<input type='checkbox' id='"
									+ obj.id
									+ "_qxf_checkbox' style='width:12px;'>全选";
							data.unshift(option);
							var comboboxObj = makeCombobox_common_dictionary(
									obj, data);
							comboboxObj.combobox("setValue", []);
							comboboxObj.combobox("setText", "请选择...");
						} else {// 其它情况是默认值传递过来的情况
							var option = {};
							option.ID = "";
							option.TEXT = "请选择...";
							data.unshift(option);
							var comboboxObj = makeCombobox_common_dictionary(
									obj, data);
							var arrDefaultV = (obj.defaultVal + "").split(",");
							comboboxObj.combobox("setValues", arrDefaultV);
						}
					} else {// 单选的话加上请选择,默认单选
						if (obj.defaultVal=="first") {
							var defaultVal='';
							if (data != null && data.length > 0) {
								defaultVal = data[0][valuekey];

							}
							$("#" + obj.id).combobox({
								valueField : valuekey,
								textField : textkey,
								panelHeight:180,
								multiple : false,
								editable : false,
								value : defaultVal,
								data : data
							});
						}else if(obj.defaultVal=="last"){
							var lastVal='';
							if (data != null && data.length > 0) {
								lastVal = data[data.length-1][valuekey];

							}
							$("#" + obj.id).combobox({
								valueField : valuekey,
								textField : textkey,
								panelHeight:180,
								multiple : false,
								editable : false,
								value : lastVal,
								data : data
							});
						} else {
							var option = {};
							option.ID = "-1";
							option.TEXT = "请选择...";
							data.unshift(option);
							$("#" + obj.id).combobox({
								valueField : valuekey,
								textField : textkey,
								panelHeight:180,
								multiple : false,
								editable : false,
								value : obj.defaultVal,
								data : data
							});
						}
					}
				}
			});
	return $("#" + obj.id);
}

function makeCombobox_common_dictionary(obj, data) {
	var textkey = obj.textkey;
	var valuekey = obj.valuekey;
	$("#" + obj.id).combobox(
			{
				valueField : valuekey,
				textField : textkey,
				multiple : true,
				editable : false,
				panelHeight:180,
				data : data,
				onSelect : function(record) {
					var qxf_flag = $("#" + obj.id + "_qxf_checkbox").attr(
							"checked");// 全选是否选中标示
					if (record[valuekey] == "") {// 选中全选
						var vals = new Array();
						if (typeof (qxf_flag) != "undefined"
								&& qxf_flag == "checked") {// 全选是否选中标示
							for (var i = 0; i < data.length; i++) {
								if (data[i][valuekey] != "") {
									vals.push(data[i][valuekey] + '');
								}
							}
							$(this).combobox("setValues", vals);
						} else {
							$(this).combobox("reset");
							$(this).combobox("setText", "请选择...");
						}
					} else {
						var hasCheckeds = $(this).combobox("getValues");

						if (hasCheckeds.length == data.length - 1) {// 去除没有意义的全选
							$("#" + obj.id + "_qxf_checkbox").attr("checked",
									"checked");
						} else {
							$("#" + obj.id + "_qxf_checkbox").attr("checked",
									false);
						}
						if (hasCheckeds.length == 0) {
							$(this).combobox("reset");
							$(this).combobox("setText", "请选择...");
						} else {
							$(this).combobox("setValues",
									sortMaoPao_common_dictionary(hasCheckeds));
						}
					}
				},
				onUnselect : function(record) {
					var qxf_flag = $("#" + obj.id + "_qxf_checkbox").attr(
							"checked");// 全选是否选中标示
					if (record[valuekey] == "") {// 选中全选
						var vals = new Array();
						if (typeof (qxf_flag) != "undefined"
								&& qxf_flag == "checked") {// 全选是否选中标示
							for (var i = 0; i < data.length; i++) {
								if (data[i][valuekey] != "") {
									vals.push(data[i][valuekey] + '');
								}
							}
							$(this).combobox("setValues", vals);
						} else {
							$(this).combobox("reset");
							$(this).combobox("setText", "请选择...");
						}
					} else {
						var hasCheckeds = $(this).combobox("getValues");
						if (hasCheckeds.length == data.length - 1) {// 去除没有意义的全选
							$("#" + obj.id + "_qxf_checkbox").attr("checked",
									"checked");
						} else {
							$("#" + obj.id + "_qxf_checkbox").attr("checked",
									false);
						}
						if (hasCheckeds.length == 0) {
							$(this).combobox("reset");
							$(this).combobox("setText", "请选择...");
						} else {
							$(this).combobox("setValues",
									sortMaoPao_common_dictionary(hasCheckeds));
						}
					}

				}
			});

	return $("#" + obj.id);
}
/*-------------------------------------通用字典-----------------------------------------------*/
function sortMaoPao_common_dictionary(hasCheckeds) {
	// 冒泡排序
	for (var i = 0; i < hasCheckeds.length; i++) {
		for (var j = i + 1; j < hasCheckeds.length; j++) {
			var temp = "";
			var val_i = hasCheckeds[i];
			var val_j = hasCheckeds[j];
			if (val_i > val_j) {
				temp = hasCheckeds[j];
				hasCheckeds[j] = hasCheckeds[i];
				hasCheckeds[i] = temp;
			}
		}
	}
	return hasCheckeds;
}
