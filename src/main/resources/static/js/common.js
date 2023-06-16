/**
 * 2023.05.08
 */


/*
option = {
	url:""
	,requestData:{}
	,completeFn: function(xhr){}
	,beforeSendFn: function(xhr){}
	,type : "GET"
};
*/
let callAsyncUrl = function(option){
	if(typeof(option) === "object" && !Array.isArray(option)){
		if(option.hasOwnProperty("url") && option["url"] !== ""){
			let url = option["url"];
			let requestData = option["requestData"];
			let completeFn = option["completeFn"];
			let beforeSendFn = option["beforeSendFn"];
			let type = option.hasOwnProperty("type") ? option["type"] : "POST";
			
			$.ajax({
				url: url
				,async: true //false 일 경우 동기 요청으로 변경
				,contentType: "application/json; charset=utf-8;"
				,type: type //"POST" //POST, PUT, GET
				,data: JSON.stringify(requestData) //전송할 데이터
				,dataType:'json'// xml, json, script, html
				,beforeSend:function(xhr) {
					return beforeSendFn(xhr);
				}// 서버 요청 전 호출 되는 함수 return false; 일 경우 요청 중단
				,complete:function(xhr) {
					completeFn(xhr);
				}// 요청의 실패, 성공과 상관 없이 완료 될 경우 호출
				,success:function(xhr) {}// 요청 완료 시
				,error:function(xhr) {}// 요청 실패.
			});
		}else{
			console.error("url 속성의 값이 없습니다: '" + JSON.stringify(option) + "'");
		}
		
	}else{
		console.error("callAsyncUrl의 인자는 object 형식만 허용합니다.");
		console.error(option);
		alert("callAsyncUrl의 인자는 object 형식만 허용합니다.");
	}
};

let tempFormArr = [];
let engineFormMap = {};
//카메라
tempFormArr.push('<table border="0px" width="100%" height="230" style="font-size: 10pt; border-spacing: 0;">                                                     ');
tempFormArr.push('	<caption>카메라 변환 요소</caption>                                                                                                                ');
tempFormArr.push('	<tbody>                                                                                                                                      ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                        ');
tempFormArr.push('			<td><input type="checkbox" name="cameraCheck" id="cameraFOV" value=""><label for="cameraFOV">화각 변환</label></td>                                                                ');
tempFormArr.push('			<td>                                                                                                                                 ');
tempFormArr.push('				수평범위(min,max)<input id="cameraFOVHmin" type="text" style="width: 55px;"/> <input id="cameraFOVHmax" type="text" style="width: 55px;"/><br/>  ');
tempFormArr.push('				수직범위(min,max)<input id="cameraFOVVmin" type="text" style="width: 55px;"/> <input id="cameraFOVVmax" type="text" style="width: 55px;"/>       ');
tempFormArr.push('			</td>                                                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                    ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                        ');
tempFormArr.push('			<td><input type="checkbox" name="cameraCheck" id="cameraResolution" value=""><label for="cameraResolution">해상도 변환</label></td>                                                              ');
tempFormArr.push('			<td>                                                                                                                                 ');
tempFormArr.push('				수평<input id="cameraResolutionWidth" type="text" style="width: 55px;"/>                                                                                      ');
tempFormArr.push('				수직<input id="cameraResolutionHeight" type="text" style="width: 55px;"/>                                                                                      ');
tempFormArr.push('			</td>                                                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                    ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                        ');
tempFormArr.push('			<td><input type="checkbox" name="cameraCheck" id="cameraDistortion" value=""><label for="cameraDistortion">왜곡변환</label></td>                                                                ');
tempFormArr.push('			<td>                                                                                                                                 ');
tempFormArr.push('				<input id="cameraDistortion0" type="text" style="width: 55px;"/>                                                                                        ');
tempFormArr.push('				<input id="cameraDistortion1" type="text" style="width: 55px;"/>                                                                                        ');
tempFormArr.push('				<input id="cameraDistortion2" type="text" style="width: 55px;"/>                                                                                        ');
tempFormArr.push('				<input id="cameraDistortion3" type="text" style="width: 55px;"/>                                                                                        ');
tempFormArr.push('				<input id="cameraDistortion4" type="text" style="width: 55px;"/>                                                                                        ');
tempFormArr.push('			</td>                                                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                    ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                        ');
tempFormArr.push('			<td><input type="checkbox" name="cameraCheck" id="cameraPosition" value="위치"><label for="cameraPosition">센서 위치 변환(변화량)</label></td>                                                     ');
tempFormArr.push('			<td>                                                                                                                                 ');
tempFormArr.push('				ΔX<input id="cameraPositionX" type="text" style="width: 55px;"/>                                                                         ');
tempFormArr.push('				ΔY<input id="cameraPositionY" type="text" style="width: 55px;"/>                                                                         ');
tempFormArr.push('				ΔZ<input id="cameraPositionZ" type="text" style="width: 55px;"/>                                                                         ');
tempFormArr.push('			</td>                                                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                    ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                        ');
tempFormArr.push('			<td><input type="checkbox" name="cameraCheck" id="cameraAngle" value="각도"><label for="cameraAngle">센서 각도 변환</label></td>                                                          ');
tempFormArr.push('			<td>                                                                                                                                 ');
tempFormArr.push('				Δroll<input id="cameraAngleRoll" type="text" style="width: 55px;"/>                                                                                   ');
tempFormArr.push('				Δpitch<input id="cameraAnglePitch" type="text" style="width: 55px;"/>                                                                                  ');
tempFormArr.push('				Δyaw<input id="cameraAngleYaw" type="text" style="width: 55px;"/>                                                                                    ');
tempFormArr.push('			</td>                                                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                    ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                        ');
tempFormArr.push('			<td><input type="checkbox" name="cameraCheck" id="cameraColor" value="특성"><label for="cameraColor">색상특성변환</label></td>                                                            ');
tempFormArr.push('			<td>                                                                                                                                 ');
tempFormArr.push('				<select id="cameraColorOption" style="width: 200px; height:23px;" >                                        ');
tempFormArr.push('					<option value="1" selected="selected">색상특성-1</option>                                                                  ');
tempFormArr.push('					<option value="2" >색상특성-2</option>                                                                                     ');
tempFormArr.push('					<option value="3" >색상특성-3</option>                                                                                     ');
tempFormArr.push('					<option value="4" >색상특성-4</option>                                                                                     ');
tempFormArr.push('					<option value="5" >색상특성-5</option>                                                                                     ');
tempFormArr.push('				</select>                                                                                                                        ');
tempFormArr.push('			</td>                                                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                    ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                        ');
tempFormArr.push('			<td style="padding-left: 20px;">변환 후 데이터명</td>                                                                                                                                ');
tempFormArr.push('			<td>                                                                                                                     ');
tempFormArr.push('				<input type="button" onclick="profileValidate(\'camera\')"; value="변환 유효성 분석" style="width: 130px; height: 28px; float:right; margin: 0 5px 0 0;"/> <input id="cameraNewFolderName" type="text" style="width: 200px; height: 22px;"/>                    ');
tempFormArr.push('			</td>                                                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                    ');
tempFormArr.push('	</tbody>                                                                                                                                     ');
tempFormArr.push('</table>                                                                                                                                       ');
engineFormMap["camera"] = tempFormArr.join("");

//라이다
tempFormArr = [];
tempFormArr.push('<table border="0px" width="100%" height="200" style="font-size: 10pt; border-spacing: 0;">                                                     ');
tempFormArr.push('	<caption>라이다 변환 요소</caption>                                                                                                                ');
tempFormArr.push('	<tbody>                                                                                                                                      ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                        ');
tempFormArr.push('			<td><input type="checkbox" name="lidarCheck" id="lidarFOV" value=""><label for="lidarFOV">화각 변환</label></td>                                                                ');
tempFormArr.push('			<td>                                                                                                                                 ');
tempFormArr.push('				수평범위(min,max)<input id="lidarFOVHmin" type="text" style="width: 55px;"/> <input id="lidarFOVHmax" type="text" style="width: 55px;"/><br/>  ');
tempFormArr.push('				수직범위(min,max)<input id="lidarFOVVmin" type="text" style="width: 55px;"/> <input id="lidarFOVVmax" type="text" style="width: 55px;"/>       ');
tempFormArr.push('			</td>                                                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                    ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                        ');
tempFormArr.push('			<td><input type="checkbox" name="lidarCheck" id="lidarChannel" value="특성"><label for="lidarChannel">채널 변환</label></td>                                                             ');
tempFormArr.push('			<td>                                                                                                                                 ');
tempFormArr.push('				<input id="lidarChannelSize" type="text" style="width: 55px;"/>                                                                                        ');
tempFormArr.push('			</td>                                                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                    ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                        ');
tempFormArr.push('			<td><input type="checkbox" name="lidarCheck" id="lidarPosition" value="위치"><label for="lidarPosition">센서 위치 변환(변화량)</label></td>                                                     ');
tempFormArr.push('			<td>                                                                                                                                 ');
tempFormArr.push('				ΔX<input id="lidarPositionX" type="text" style="width: 55px;"/>                                                                         ');
tempFormArr.push('				ΔY<input id="lidarPositionY" type="text" style="width: 55px;"/>                                                                         ');
tempFormArr.push('				ΔZ<input id="lidarPositionZ" type="text" style="width: 55px;"/>                                                                         ');
tempFormArr.push('			</td>                                                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                    ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                        ');
tempFormArr.push('			<td><input type="checkbox" name="lidarCheck" id="lidarAngle" value="각도"><label for="lidarAngle">센서 각도 변환</label></td>                                                          ');
tempFormArr.push('			<td>                                                                                                                                 ');
tempFormArr.push('				Δroll<input id="lidarAngleRoll" type="text" style="width: 55px;"/>                                                                                   ');
tempFormArr.push('				Δpitch<input id="lidarAnglePitch" type="text" style="width: 55px;"/>                                                                                  ');
tempFormArr.push('				Δyaw<input id="lidarAngleYaw" type="text" style="width: 55px;"/>                                                                                    ');
tempFormArr.push('			</td>                                                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                    ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                        ');
tempFormArr.push('			<td style="padding-left: 20px;">변환 후 데이터명</td>                                                                                                                                ');
tempFormArr.push('			<td>                                                                                                                     ');
tempFormArr.push('				<input type="button" onclick="profileValidate(\'lidar\')"; value="변환 유효성 분석" style="width: 130px; height: 28px; float:right; margin: 0 5px 0 0;"/> <input id="lidarNewFolderName" type="text" style="width: 200px; height: 22px;"/>                   ');
tempFormArr.push('			</td>                                                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                    ');
tempFormArr.push('	</tbody>                                                                                                                                     ');
tempFormArr.push('</table>                                                                                                                                       ');
engineFormMap["lidar"] = tempFormArr.join("");

//레이다
tempFormArr = [];
tempFormArr.push('<table border="0px" width="100%" height="200" style="font-size: 10pt; border-spacing: 0;">                                                     ');
tempFormArr.push('	<caption>레이다 변환 요소</caption>                                                                                                                ');
tempFormArr.push('	<tbody>                                                                                                                                      ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                        ');
tempFormArr.push('			<td><input type="checkbox" name="radarCheck" id="radarFOV" value=""><label for="radarFOV">화각 변환</label></td>                                                                ');
tempFormArr.push('			<td>                                                                                                                                 ');
tempFormArr.push('				수평범위(min,max)<input id="radarFOVHmin" type="text" style="width: 55px;"/> <input id="radarFOVHmax" type="text" style="width: 55px;"/><br/>  ');
tempFormArr.push('				수직범위(min,max)<input id="radarFOVVmin" type="text" style="width: 55px;"/> <input id="radarFOVVmax" type="text" style="width: 55px;"/>       ');
tempFormArr.push('			</td>                                                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                    ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                        ');
tempFormArr.push('			<td><input type="checkbox" name="radarCheck" id="radarPosition" value="위치"><label for="radarPosition">센서 위치 변환(변화량)</label></td>                                                     ');
tempFormArr.push('			<td>                                                                                                                                 ');
tempFormArr.push('				ΔX<input id="radarPositionX" type="text" style="width: 55px;"/>                                                                         ');
tempFormArr.push('				ΔY<input id="radarPositionY" type="text" style="width: 55px;"/>                                                                         ');
tempFormArr.push('				ΔZ<input id="radarPositionZ" type="text" style="width: 55px;"/>                                                                         ');
tempFormArr.push('			</td>                                                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                    ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                        ');
tempFormArr.push('			<td><input type="checkbox" name="radarCheck" id="radarAngle" value="각도"><label for="radarAngle">센서 각도 변환</label></td>                                                          ');
tempFormArr.push('			<td>                                                                                                                                 ');
tempFormArr.push('				Δroll<input id="radarAngleRoll" type="text" style="width: 55px;"/>                                                                                   ');
tempFormArr.push('				Δpitch<input id="radarAnglePitch" type="text" style="width: 55px;"/>                                                                                  ');
tempFormArr.push('				Δyaw<input id="radarAngleYaw" type="text" style="width: 55px;"/>                                                                                    ');
tempFormArr.push('			</td>                                                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                    ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                        ');
tempFormArr.push('			<td style="padding-left: 20px;">변환 후 데이터명</td>                                                                                                                                ');
tempFormArr.push('			<td>                                                                                                                     ');
tempFormArr.push('				<input type="button" onclick="profileValidate(\'radar\')"; value="변환 유효성 분석" style="width: 130px; height: 28px; float:right; margin: 0 5px 0 0;"/> <input id="radarNewFolderName" type="text" style="width: 200px; height: 22px;"/>                   ');
tempFormArr.push('			</td>                                                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                    ');
tempFormArr.push('	</tbody>                                                                                                                                     ');
tempFormArr.push('</table>                                                                                                                                       ');
engineFormMap["radar"] = tempFormArr.join("");



//format
tempFormArr = [];
tempFormArr.push('정의된 컨텐츠가 없습니다');
engineFormMap["format"] = tempFormArr.join("");

let profileFormMap = {};
tempFormArr = [];
tempFormArr.push('<table border="0px" width="100%" height="200" style="font-size: 10pt; border-spacing: 0;">                                                   ');
tempFormArr.push('	<caption>입력 데이터 프로파일</caption>                                                                                                        ');
tempFormArr.push('	<tbody>                                                                                                                                    ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>센서 종류</td>                                                                                                                 ');
tempFormArr.push('			<td>                                                                                                                               ');
tempFormArr.push('				<input type="radio" name="sensorType" id="camera" value="camera" onclick="return(false);" checked="checked"><label for="camera">카메라</label>          ');
tempFormArr.push('				<input type="radio" name="sensorType" id="lidar" value="lidar" onclick="return(false);"><label for="lidar">라이다</label>                               ');
tempFormArr.push('				<input type="radio" name="sensorType" id="radar" value="radar" onclick="return(false);"><label for="radar">레이다</label>                               ');
tempFormArr.push('			</td>                                                                                                                              ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>센서 모델명</td>                                                                                                               ');
tempFormArr.push('			<td><span id="model"></span></td>                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>총 데이터량</td>                                                                                                               ');
tempFormArr.push('			<td><span id="Data_amount"></span></td>                                                                                                             ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>센서 수평 화각 범위(min,max)</td>                                                                                              ');
tempFormArr.push('			<td>(<span id="FOV_Horizontal_min"></span>, <span id="FOV_Horizontal_max"></span>)</td>                                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>센서 수직 화각 범위(min,max)</td>                                                                                              ');
tempFormArr.push('			<td>(<span id="FOV_Vertical_min"></span>, <span id="FOV_Vertical_max"></span>)</td>                                             ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>센서 해상도(수평,수직)</td>                                                                                                    ');
tempFormArr.push('			<td>(<span id="image_width"></span>, <span id="image_height"></span>)</td>                                                                                     ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>센서 차량 장착 위치(X,Y,Z)</td>                                                                                                ');
tempFormArr.push('			<td>(<span id="position_x"></span>, <span id="position_y"></span>, <span id="position_z"></span>)</td>                                                                                                        ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>센서 차량 장착 각도(roll,pitch,yaw) </td>                                                                                      ');
tempFormArr.push('			<td>(<span id="angle_roll"></span>, <span id="angle_pitch"></span>, <span id="angle_yaw"></span>)</td>                                                                                                    ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>렌즈 왜곡 계수[k1, k2, p1, p2, k3]</td>                                                                                                    ');
tempFormArr.push('			<td><span id="distortion_coefficients"></span></td>                                                                               ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('	</tbody>                                                                                                                                   ');
tempFormArr.push('</table>                                                                                                                                     ');
profileFormMap["camera"] = tempFormArr.join("");


tempFormArr = [];
tempFormArr.push('<table border="0px" width="100%" height="200" style="font-size: 10pt; border-spacing: 0;">                                                   ');
tempFormArr.push('	<caption>입력 데이터 프로파일</caption>                                                                                                        ');
tempFormArr.push('	<tbody>                                                                                                                                    ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>센서 종류</td>                                                                                                                 ');
tempFormArr.push('			<td>                                                                                                                               ');
tempFormArr.push('				<input type="radio" name="sensorType" id="camera" value="camera" onclick="return(false);"><label for="camera">카메라</label>          ');
tempFormArr.push('				<input type="radio" name="sensorType" id="lidar" value="lidar" onclick="return(false);" checked="checked"><label for="lidar">라이다</label>                               ');
tempFormArr.push('				<input type="radio" name="sensorType" id="radar" value="radar" onclick="return(false);"><label for="radar">레이다</label>                               ');
tempFormArr.push('			</td>                                                                                                                              ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>센서 모델명</td>                                                                                                               ');
tempFormArr.push('			<td><span id="model"></span></td>                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>총 데이터량</td>                                                                                                               ');
tempFormArr.push('			<td><span id="data_amount"></span></td>                                                                                                             ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>채널개수</td>                                                                                                               ');
tempFormArr.push('			<td><span id="channels"></span></td>                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>센서 수평 화각 범위(min,max)</td>                                                                                              ');
tempFormArr.push('			<td>(<span id="fov_horizontal_min"></span>, <span id="fov_horizontal_max"></span>)</td>                                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>센서 수직 화각 범위(min,max)</td>                                                                                              ');
tempFormArr.push('			<td>(<span id="fov_vertical_min"></span>, <span id="fov_vertical_max"></span>)</td>                                             ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>센서 해상도(수평,수직)</td>                                                                                                    ');
tempFormArr.push('			<td>(<span id="horizontal_resolution"></span>, <span id="vertical_resolution"></span>)</td>                                                                                     ');
tempFormArr.push('		</tr>                                                                                                                                  ');

tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>센서 차량 장착 위치(X,Y,Z)</td>                                                                                                ');
tempFormArr.push('			<td>(<span id="position_x"></span>, <span id="position_y"></span>, <span id="position_z"></span>)</td>                                                                                                        ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>센서 차량 장착 각도(roll,pitch,yaw) </td>                                                                                      ');
tempFormArr.push('			<td>(<span id="angle_roll"></span>, <span id="angle_pitch"></span>, <span id="angle_yaw"></span>)</td>                                                                                                    ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('	</tbody>                                                                                                                                   ');
tempFormArr.push('</table>                                                                                                                                     ');
profileFormMap["lidar"] = tempFormArr.join("");

tempFormArr = [];
tempFormArr.push('<table border="0px" width="100%" height="200" style="font-size: 10pt; border-spacing: 0;">                                                   ');
tempFormArr.push('	<caption>입력 데이터 프로파일</caption>                                                                                                        ');
tempFormArr.push('	<tbody>                                                                                                                                    ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>센서 종류</td>                                                                                                                 ');
tempFormArr.push('			<td>                                                                                                                               ');
tempFormArr.push('				<input type="radio" name="sensorType" id="camera" value="camera" onclick="return(false);"><label for="camera">카메라</label>          ');
tempFormArr.push('				<input type="radio" name="sensorType" id="lidar" value="lidar" onclick="return(false);"><label for="lidar">라이다</label>                               ');
tempFormArr.push('				<input type="radio" name="sensorType" id="radar" value="radar" onclick="return(false);" checked="checked"><label for="radar">레이다</label>                               ');
tempFormArr.push('			</td>                                                                                                                              ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>센서 모델명</td>                                                                                                               ');
tempFormArr.push('			<td><span id="model"></span></td>                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>총 데이터량</td>                                                                                                               ');
tempFormArr.push('			<td><span id="data_amount"></span></td>                                                                                                             ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>센서 수평 화각 범위(min,max)</td>                                                                                              ');
tempFormArr.push('			<td>(<span id="fOV_horizontal_min"></span>, <span id="fOV_horizontal_max"></span>)</td>                                                                                                                ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>센서 수직 화각 범위(min,max)</td>                                                                                              ');
tempFormArr.push('			<td>(<span id="fOV_vertical_min"></span>, <span id="fOV_vertical_max"></span>)</td>                                             ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>센서 차량 장착 각도(roll,pitch,yaw) </td>                                                                                      ');
tempFormArr.push('			<td>(<span id="angle_roll"></span>, <span id="angle_pitch"></span>, <span id="angle_yaw"></span>)</td>                                                                                                    ');
tempFormArr.push('		</tr>                                                                                                                                  ');






tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>범위(min,max)</td>                                                                                                    ');
tempFormArr.push('			<td>(<span id="range_min"></span>, <span id="range_max"></span>)</td>                                                                                     ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('		<tr align="left" bgcolor="white">                                                                                                      ');
tempFormArr.push('			<td>센서 차량 장착 위치(X,Y,Z)</td>                                                                                                ');
tempFormArr.push('			<td>(<span id="position_x"></span>, <span id="position_y"></span>, <span id="position_z"></span>)</td>                                                                                                        ');
tempFormArr.push('		</tr>                                                                                                                                  ');
tempFormArr.push('	</tbody>                                                                                                                                   ');
tempFormArr.push('</table>                                                                                                                                     ');
profileFormMap["radar"] = tempFormArr.join("");

tempFormArr = [];
tempFormArr.push('정의된 컨텐츠가 없습니다');
profileFormMap["format"] = tempFormArr.join("");








function setProfileValue(profile){
	for(let key in profile) {
//		console.log(key + ":" + JSON.stringify(profile[key]) + ":" + typeof(profile[key]));
//		console.log(document.getElementById(key));
		
		if(key !== "sensor" && key !== "camera_model" && key.lastIndexOf("matrix") === -1){//표시하지 않을 key 필터링
			if((typeof(profile[key]) === "string" || typeof(profile[key]) === "number") && document.getElementById(key) !== null){
				document.getElementById(key).innerText = profile[key];
//				console.log("OK-1");
			}else{
				for(let sub_1_key in profile[key]) {
					if(profile[key].hasOwnProperty("data") && document.getElementById(key) !== null){
						document.getElementById(key).innerText = JSON.stringify(profile[key]["data"]);
//						console.log("OK-2");
					}else if(document.getElementById(key + "_" + sub_1_key) !== null){
						document.getElementById(key + "_" + sub_1_key).innerText = profile[key][sub_1_key];
//						console.log(document.getElementById(key + "_" + sub_1_key));
					}else{
						//FOV 경우
						for(let sub_2_key in profile[key][sub_1_key]) {
							if(document.getElementById(key + "_" + sub_1_key + "_" + sub_2_key) !== null){
								document.getElementById(key + "_" + sub_1_key + "_" + sub_2_key).innerText = profile[key][sub_1_key][sub_2_key];
							}else{
								console.log(key + "::" + sub_1_key + "::" + JSON.stringify(profile[key][sub_1_key]) + "::" + typeof(profile[key][sub_1_key]));
								console.log(sub_2_key + "::" + JSON.stringify(profile[key][sub_1_key][sub_2_key]) + "::" + typeof(profile[key][sub_1_key][sub_2_key]));
							}
						}
					}
				}
			}
		}
		
	}
}




















