<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<img src="https://s3.amazonaws.com/2ndscreentvshow/${json.choosenSeries}" alt="teste"><br/>

<c:set var="series" value="${json.series}"/>
${series[0]}
${json.tvShowName}<br/><br/>
${json.network}   <br/>     <br/>
${json.firstAired}     <br/>     <br/>
${json.overview}            <br/>     <br/>
<c:forEach items="${json.cast}" var="casting">
    <img src="https://s3.amazonaws.com/2ndscreentvshow/${casting.roleImage}" alt="teste"><br/>
    ${casting.role} -  ${casting.castName.name}<br/>
    ${casting.castName.type}<br/>
</c:forEach>
${json.airsTime}                      <br/> <br/>
<c:forEach items="${json.genre}" var="item">
    ${item}<br/>
</c:forEach>                              <br/> <br/>
${json.runTime}                                 <br/><br/>
<c:forEach items="${json.seasonMap}" var="seasonItem">
        <img src="https://s3.amazonaws.com/2ndscreentvshow/${seasonItem.value.chooseSeasonURL}" alt="seasonItem.value."><br/>
        Season ${seasonItem.key}                                                                 <br/>
</c:forEach>
