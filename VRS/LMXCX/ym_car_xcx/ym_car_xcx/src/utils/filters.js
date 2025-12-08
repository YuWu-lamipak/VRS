// 酒水选择
function achievementStatus(status) {
	let statusName = ''
	switch (status) {
		case 0:
			statusName = "考试未通过";
			break;
		case 1:
			statusName = "考试通过";
			break; 
	}
	return statusName;
}
// 就餐形式 1客饭、2和餐（无冷盘）、3桌餐
function getDiningStyle(status) {
	let statusName = ''
	switch (status) {
		case 1:
			statusName = "客饭";
			break;
		case 2:
			statusName = "和餐（无冷盘）";
			break;
		case 3:
			statusName = "桌餐";
			break;

	}
	return statusName;
}
// 餐饮状态 
function getDiningStatus(status) {
	let statusName = ''
	switch (status) {
		case 1:
			statusName = "已申请";
			break;
		case 2:
			statusName = "已撤销";
			break;
		case 3:
			statusName = "审核通过";
			break;
		case 4:
			statusName = "审核不通过";
			break;
		case 5:
			statusName = "已答复";
			break;

	}
	return statusName;
}
// 车辆状态
function getCarStatus(status) {
	// 1已申请2已撤销3审核通过4审核不通过5审批通过、6审批不通过、7已调度、8已结束
	let statusName = ''
	switch (status) {
		case 1:
			statusName = "已申请";
			break;
		case 2:
			statusName = "已撤销";
			break;
		case 3:
			statusName = "审核通过";
			break;
		case 4:
			statusName = "审核不通过";
			break;
		case 5:
			statusName = "审批通过";
			break;
		case 6:
			statusName = "审批不通过";
			break;
		case 7:
			statusName = "已调度";
			break;
		case 8:
			statusName = "已结束";
			break;

	}
	return statusName;
}
// 车型
function getCarName(status) {
	// 1小车2商务车3面包车4考斯特
	let statusName = ''
	switch (status) {
		case 1:
			statusName = "小车";
			break;
		case 2:
			statusName = "商务车";
			break;
		case 3:
			statusName = "面包车";
			break;
		case 4:
			statusName = "考斯特";
			break;
	}
	return statusName;
}

// 1已申请、2已撤销、3审核通过、4审核不通过、 5已安排
// 车型
function getMeetStatus(status) {
	// 1小车2商务车3面包车4考斯特
	let statusName = ''
	switch (status) {
		case 1:
			statusName = "已申请";
			break;
		case 2:
			statusName = "已撤销";
			break;
		case 3:
			statusName = "审核通过";
			break;
		case 4:
			statusName = "审核不通过";
			break;
		case 5:
			statusName = "已安排";
			break;
	}
	return statusName;
}
// 会务  1已申请、2已撤销、3审核通过、4审核不通过、 5已安排
function getMeetingStatus(status) {
	// 1小车2商务车3面包车4考斯特
	let statusName = ''
	switch (status) {
		case 1:
			statusName = "已申请";
			break;
		case 2:
			statusName = "已撤销";
			break;
		case 3:
			statusName = "审核通过";
			break;
		case 4:
			statusName = "审核不通过";
			break;
		case 5:
			statusName = "已安排";
			break;
	}
	return statusName;
}


// 会务  1正常、2撤销、 
function takeoutFoodStatus(status) {
	// 1小车2商务车3面包车4考斯特
	let statusName = ''
	switch (status) {
		case 1:
			statusName = "正常";
			break;
		case 2:
			statusName = "已撤销";
			break;
	}
	return statusName;
}

export default {
	achievementStatus,
	getDiningStyle,
	getDiningStatus,
	getCarStatus,
	getCarName,
	getMeetStatus,
	getMeetingStatus,
	takeoutFoodStatus
}
