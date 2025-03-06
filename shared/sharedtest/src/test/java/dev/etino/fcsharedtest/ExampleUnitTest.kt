package dev.etino.fcsharedtest

import dev.etino.fcshared.attendance.client.AttendanceClientImpl
import dev.etino.fcshared.attendance.parser.AttendanceParser
import dev.etino.fcshared.attendance.repository.AttendanceRepositoryImpl
import dev.etino.fcshared.networking.interceptors.LoginInterceptorPluginMockImpl
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun funTestAttendanceParsing() {
        val stringToParse = """
         <div class="semster winter">

            <div class="semsterHeader"> Zimski semstar </div>
        
                <div class="header clearfix">
                <div class="cell fullName firstColumn">
                    <div class="cellContent">PREDMET</div>
                </div>
        
                <div class="categoryCells">
                        <div class="cell Predavanja" style="width: 33.3333333333333%;">
                            <div class="cellContent">
                                Predavanja
                            </div>
                        </div>
                        <div class="cell Auditorne-vjezbe" style="width: 33.3333333333333%;">
                            <div class="cellContent">
                                Auditorne vježbe
                            </div>
                        </div>
                        <div class="cell Laboratorijske-vjezbe" style="width: 33.3333333333333%;">
                            <div class="cellContent">
                                Laboratorijske vježbe
                            </div>
                        </div>
                </div>
            </div>
                <div class="body clearfix">
                        <a href="/prisutnost/predmeti/9291" 
                            class="row clearfix odd">
                            
                            <div class="cell fullName firstColumn">
                                <div class="cellContent">  Grid računalni sustavi  </div>
                            </div>
                            
                            <div class="categoryCells">
                                        <div class="cell category Bad" style="width: 33.3333333333333%;">
                                            <div class="cellContent clearfix">
                                                    <span class="categoryAttendancePercent">
                                                        45 %
                                                    </span>
                                                    <span class="categoryAttendance">
                                                        5 / 11 
                                                    </span>
                                            </div>
                                        </div>
                                        <div class="cell category Great" style="width: 33.3333333333333%;">
                                            <div class="cellContent clearfix">
                                                    <span class="categoryAttendancePercent">
                                                        75 %
                                                    </span>
                                                    <span class="categoryAttendance">
                                                        9 / 12 
                                                    </span>
                                            </div>
                                        </div>
                                        <div class="cell category" style="width: 33.3333333333333%;">
                                            <div class="cellContent">
                                                <span class="categoryAttendanceEmpty"></span>
                                            </div>
                                        </div>
                            </div>
                        </a>
                        <a href="/prisutnost/predmeti/9284" 
                            class="row clearfix even">
                            
                            <div class="cell fullName firstColumn">
                                <div class="cellContent">  Multimedijski sustavi  </div>
                            </div>
                            
                            <div class="categoryCells">
                                        <div class="cell category Bad" style="width: 33.3333333333333%;">
                                            <div class="cellContent clearfix">
                                                    <span class="categoryAttendancePercent">
                                                        9 %
                                                    </span>
                                                    <span class="categoryAttendance">
                                                        1 / 11 
                                                    </span>
                                            </div>
                                        </div>
                                        <div class="cell category" style="width: 33.3333333333333%;">
                                            <div class="cellContent">
                                                <span class="categoryAttendanceEmpty"></span>
                                            </div>
                                        </div>
                                        <div class="cell category Bad" style="width: 33.3333333333333%;">
                                            <div class="cellContent clearfix">
                                                    <span class="categoryAttendancePercent">
                                                        90 %
                                                    </span>
                                                    <span class="categoryAttendance">
                                                        10 / 11 
                                                    </span>
                                            </div>
                                        </div>
                            </div>
                        </a>
                        <a href="/prisutnost/predmeti/9298" 
                            class="row clearfix odd">
                            
                            <div class="cell fullName firstColumn">
                                <div class="cellContent">  Paralelno programiranje  </div>
                            </div>
                            
                            <div class="categoryCells">
                                        <div class="cell category Bad" style="width: 33.3333333333333%;">
                                            <div class="cellContent clearfix">
                                                    <span class="categoryAttendancePercent">
                                                        20 %
                                                    </span>
                                                    <span class="categoryAttendance">
                                                        2 / 10 
                                                    </span>
                                            </div>
                                        </div>
                                        <div class="cell category" style="width: 33.3333333333333%;">
                                            <div class="cellContent">
                                                <span class="categoryAttendanceEmpty"></span>
                                            </div>
                                        </div>
                                        <div class="cell category Bad" style="width: 33.3333333333333%;">
                                            <div class="cellContent clearfix">
                                                    <span class="categoryAttendancePercent">
                                                        87 %
                                                    </span>
                                                    <span class="categoryAttendance">
                                                        7 / 8 
                                                    </span>
                                            </div>
                                        </div>
                            </div>
                        </a>
                        <a href="/prisutnost/predmeti/9292" 
                            class="row clearfix even">
                            
                            <div class="cell fullName firstColumn">
                                <div class="cellContent">  Poslovni informacijski sustavi  </div>
                            </div>
                            
                            <div class="categoryCells">
                                        <div class="cell category Bad" style="width: 33.3333333333333%;">
                                            <div class="cellContent clearfix">
                                                    <span class="categoryAttendancePercent">
                                                        23 %
                                                    </span>
                                                    <span class="categoryAttendance">
                                                        3 / 13 
                                                    </span>
                                            </div>
                                        </div>
                                        <div class="cell category" style="width: 33.3333333333333%;">
                                            <div class="cellContent">
                                                <span class="categoryAttendanceEmpty"></span>
                                            </div>
                                        </div>
                                        <div class="cell category Bad" style="width: 33.3333333333333%;">
                                            <div class="cellContent clearfix">
                                                    <span class="categoryAttendancePercent">
                                                        75 %
                                                    </span>
                                                    <span class="categoryAttendance">
                                                        9 / 12 
                                                    </span>
                                            </div>
                                        </div>
                            </div>
                        </a>
                        <a href="/prisutnost/predmeti/9286" 
                            class="row clearfix odd">
                            
                            <div class="cell fullName firstColumn">
                                <div class="cellContent">  Sigurnost bežičnih mreža  </div>
                            </div>
                            
                            <div class="categoryCells">
                                        <div class="cell category Great" style="width: 33.3333333333333%;">
                                            <div class="cellContent clearfix">
                                                    <span class="categoryAttendancePercent">
                                                        85 %
                                                    </span>
                                                    <span class="categoryAttendance">
                                                        12 / 14 
                                                    </span>
                                            </div>
                                        </div>
                                        <div class="cell category" style="width: 33.3333333333333%;">
                                            <div class="cellContent">
                                                <span class="categoryAttendanceEmpty"></span>
                                            </div>
                                        </div>
                                        <div class="cell category Bad" style="width: 33.3333333333333%;">
                                            <div class="cellContent clearfix">
                                                    <span class="categoryAttendancePercent">
                                                        91 %
                                                    </span>
                                                    <span class="categoryAttendance">
                                                        11 / 12 
                                                    </span>
                                            </div>
                                        </div>
                            </div>
                        </a>
                        <a href="/prisutnost/predmeti/9293" 
                            class="row clearfix even">
                            
                            <div class="cell fullName firstColumn">
                                <div class="cellContent">  Ugradbeni računalni sustavi  </div>
                            </div>
                            
                            <div class="categoryCells">
                                        <div class="cell category Great" style="width: 33.3333333333333%;">
                                            <div class="cellContent clearfix">
                                                    <span class="categoryAttendancePercent">
                                                        83 %
                                                    </span>
                                                    <span class="categoryAttendance">
                                                        10 / 12 
                                                    </span>
                                            </div>
                                        </div>
                                        <div class="cell category" style="width: 33.3333333333333%;">
                                            <div class="cellContent">
                                                <span class="categoryAttendanceEmpty"></span>
                                            </div>
                                        </div>
                                        <div class="cell category" style="width: 33.3333333333333%;">
                                            <div class="cellContent">
                                                <span class="categoryAttendanceEmpty"></span>
                                            </div>
                                        </div>
                            </div>
                        </a>
                </div>
        
        
        </div>
        
        <div class="semster summer">
        
            <div class="semsterHeader"> Ljetni semstar </div>
        
                <div class="header clearfix">
                <div class="cell fullName firstColumn">
                    <div class="cellContent">PREDMET</div>
                </div>
        
                <div class="categoryCells">
                        <div class="cell Predavanja" style="width: 33.3333333333333%;">
                            <div class="cellContent">
                                Predavanja
                            </div>
                        </div>
                        <div class="cell Auditorne-vjezbe" style="width: 33.3333333333333%;">
                            <div class="cellContent">
                                Auditorne vježbe
                            </div>
                        </div>
                        <div class="cell Laboratorijske-vjezbe" style="width: 33.3333333333333%;">
                            <div class="cellContent">
                                Laboratorijske vježbe
                            </div>
                        </div>
                </div>
            </div>
        <div class="emptyList"> Nemate upisanih predmeta. </div>


</div>
    """.trimIndent()

        val parser = AttendanceParser()
        val result = parser.parse(stringToParse)

        print(result)
        assert(result.isNotEmpty())
    }

    @Test
    fun funTestAttendanceItemParsing() {
        val stringToParse = """
            
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="utf-8"/>
                <title>FESB Raspored | Grid računalni sustavi </title>
                
                <link rel="shortcut icon" href="/favicon-raspored.ico" type="image/x-icon">
                <link rel="icon" href="/favicon-raspored.ico" type="image/x-icon">
                
                <link rel="stylesheet" type="text/css" href="/combres.axd/siteCss/-1039019663/"/>
                <link rel="stylesheet" type="text/css" href="/combres.axd/mainWindowCss/-1641206846/"/>
                <script type="text/javascript" src="/combres.axd/siteJs/1462601077/"></script>
                <script type="text/javascript" src="/combres.axd/mainWindowJs/1009116408/"></script>
                
                
                
                


                
                <script type="text/javascript">
                    ${'$'}(function () {
                        var redirectUrl = '/nepodrzani-preglednik';
                        ${'$'}.unsupportedBrowser(redirectUrl);
                    });
                </script>
                

                <script type="text/javascript">

                    var _gaq = _gaq || [];
                    _gaq.push(['_setAccount', 'UA-33654893-9']);
                    _gaq.push(['_trackPageview']);

                    (function () {
                        var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
                        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                        var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
                    })();

                </script>

                <script src="https://korisnik.fesb.unist.hr/scripts/plugins/omnibar.js"></script>

                <link href="https://korisnik.fesb.unist.hr/themes/plugins/omnibar.css" rel="stylesheet" type="text/css" />

                     <script>
                        ${'$'}(function () {
                            ${'$'}('#omnibarContainer').omnibar({
                                activeClassName: 'schedule',
                                returnUrl: 'https://raspored.fesb.unist.hr'
                            });
                        });
                    </script>

            </head>
            <body>
                
                   
                <div class="mainContainer">


            <div class="backgroundTableContainer">
                <table class="backgroundTable">
                    <tr class="backgroundTable-top">
                        <td class="backgroundTable-top-left">&nbsp;</td>
                        <td class="backgroundTable-top-center">&nbsp;</td>
                        <td class="backgroundTable-top-right">&nbsp;</td>
                    </tr>

                    <tr class="backgroundTable-mid">
                        <td class="backgroundTable-mid-left">&nbsp;</td>
                        <td class="backgroundTable-mid-center">&nbsp;</td>
                        <td class="backgroundTable-mid-right">&nbsp;</td>
                    </tr>

                    <tr class="backgroundTable-bottom">
                        <td class="backgroundTable-bottom-left">&nbsp;</td>
                        <td class="backgroundTable-bottom-center">&nbsp;</td>
                        <td class="backgroundTable-bottom-right">&nbsp;</td>
                    </tr>
                </table>
            </div>

            <div class="glowImage"><img alt="" src="/Content/Images/Background/bottom-glow.png" /></div>

                    <div id="omnibarContainer"></div>
                    
                    <table class="wrapperTable">
                        <tr>
                            <td class="wrapperCell">
                                
                                <div class="mainContent">
                                    
                                    <div class="mainTile">
                                        <div class="mainTile-top"></div>
                                        <div class="mainTile-mid">
                                            <div class="mainTile-topTexture">
                                                <div class="mainTile-bottomTexture">
                                                    <div class="mainTileContent">
                                                        
                                                        <div class="topStripe">
                                                            <div class="appTitle">FESB RASPORED</div>
                                                            <div class="loading"><img src="/Content/Images/loader.gif" alt="Loading..." /></div>

                                                            
                                                        </div>
                                                        

            <ul class="mainMenu mm-StudentDashboard clearfix">
                
                <li id="mm-Calendar"> <a href=/raspored> Raspored </a> </li>

                
                    <li id="mm-StudentDashboard"> <a > Prisutnost </a> </li>
                
                
            </ul>

            <div class="menuHighlight"></div>
                                                        


            <div class="studentDashboardSection">
                
                       
            <ul class="subMenu sm-Courses clearfix">
                <li id="sm-General"> <a href=/prisutnost/opcenito> Općenito </a> </li>   
                <li id="sm-Courses"> <a > Predmeti </a> </li>
            </ul>
                


            <script type="text/javascript">
                ${'$'}(function () {
                    Dump.TextToImageReplacer.init('.studentDashboardSection .course h1, .courseGeneralPage .coursePart .courseTitle');
                    
                    Dump.Utility.setElementHeight('.studentDashboardSection .jscroll-wrapper', function () { return ${'$'}('.studentDashboardSection .coursePart').innerHeight() - 66; });
                }); 
            </script>

            <div class="coursesPage clearfix">
                <div class="coursesPart">
                    <div class="separatorTop"></div>
                    <div class="separatorMid coursesPartContent">
                        

            <script type="text/javascript">

                ${'$'}(function () {
                    var jScrollObject = ${'$'}(".studentDashboardSection .coursesPart .jscroll-wrapper").jScrollPane({ showArrows: true, animateScroll: true, animateDuration: 150, verticalGutter: -9 });
                    var jScrollApi      = jScrollObject.data('jsp');
                    
                    ${'$'}('.coursesList.summerSemester').filterable({ inputSelector: '#courseSearch', onFiltered: function () { jScrollApi.reinitialise(); } });
                    ${'$'}('.coursesList.winterSemester').filterable({ inputSelector: '#courseSearch', onFiltered: function () { jScrollApi.reinitialise(); } });

                    ${'$'}('#courseSearch').searchBox({ watermark: 'Pretraži predmete...' });
                });
                  
            </script>

            <div class="coursesListPart">

                <input type="text" id="courseSearch" />

                <div class="jscroll-wrapper">
                    <div class="tableInlineHeader"> Zimski semstar </div>
                    <div class="coursesList winterSemester">

                        <div class="empty">Nije pronađen nijedan predmet.</div>
                        <ul>

                                <li class="ui-state-active">
                                    <a href="/prisutnost/predmeti/9291">
                                
                                        <span class="name">Grid računalni sustavi</span>
                                        <span class="details">
                                            <span class="code">FELK11:</span>
                                    
                                                <span class="studyCode">250</span>                            </span>
                                
                                    </a>
                                </li>
                                <li class="">
                                    <a href="/prisutnost/predmeti/9284">
                                
                                        <span class="name">Multimedijski sustavi</span>
                                        <span class="details">
                                            <span class="code">FELK08:</span>
                                    
                                                <span class="studyCode">250</span>                            </span>
                                
                                    </a>
                                </li>
                                <li class="">
                                    <a href="/prisutnost/predmeti/9298">
                                
                                        <span class="name">Paralelno programiranje</span>
                                        <span class="details">
                                            <span class="code">FELK35:</span>
                                    
                                                <span class="studyCode">250</span>                            </span>
                                
                                    </a>
                                </li>
                                <li class="">
                                    <a href="/prisutnost/predmeti/9292">
                                
                                        <span class="name">Poslovni informacijski sustavi</span>
                                        <span class="details">
                                            <span class="code">FETK01:</span>
                                    
                                                <span class="studyCode">250</span>                            </span>
                                
                                    </a>
                                </li>
                                <li class="">
                                    <a href="/prisutnost/predmeti/9286">
                                
                                        <span class="name">Sigurnost bežičnih mreža</span>
                                        <span class="details">
                                            <span class="code">FELK19:</span>
                                    
                                                <span class="studyCode">250</span>                            </span>
                                
                                    </a>
                                </li>
                                <li class="">
                                    <a href="/prisutnost/predmeti/9293">
                                
                                        <span class="name">Ugradbeni računalni sustavi</span>
                                        <span class="details">
                                            <span class="code">FELK12:</span>
                                    
                                                <span class="studyCode">250</span>                            </span>
                                
                                    </a>
                                </li>
                        
                        </ul>
                    </div>
                    
                </div>

            </div>
                    </div>
                    <div class="separatorBottom"> </div>
                </div>
                    
                <div class="coursePart">
                    
                    <div class="course">
                        
                        
                        <div class="heading clearfix">
                            <h1>Grid računalni sustavi</h1>    
                        </div>
                        
                        
                        <br />


            <ul class="curriculumMenu cm-General clearfix">
                
                <li id="cm-General"> 
                    <a > 
                        Općenito 
                    </a> 
                </li>   
                   
                    <li id="cm-Predavanja">
                    <a href=/prisutnost/predmeti/9291/Predavanja> 
                            Predavanja 
                        </a> 
                    </li>       
                    <li id="cm-AuditorneVjezbe">
                    <a href=/prisutnost/predmeti/9291/Auditorne-vjezbe> 
                            Auditorne vježbe 
                        </a> 
                    </li>       
                  
            </ul>
                        

            <div class="courseCategories">

                        <div class="courseCategory Predavanja clearfix">
                                
                            <div class="name"> predavanja </div>

                                <div class="numbers clearfix">
                                    <div class="attended"> <span class="num">5</span>         <span>dolazaka</span>
            </div>
                                    <div class="absent"  > <span class="num">6  </span>         <span>izostanaka</span>
            </div>
                                </div>
                                <div class="infoGraph clearfix blocks-11">

                                            <div class="block attended ">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br />dolazaka</span>
                                                <span class="required-number">8</span>
                                            </div>
                                            <div class="block attended ">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br />dolazaka</span>
                                                <span class="required-number">8</span>
                                            </div>
                                            <div class="block attended ">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br />dolazaka</span>
                                                <span class="required-number">8</span>
                                            </div>
                                            <div class="block attended ">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br />dolazaka</span>
                                                <span class="required-number">8</span>
                                            </div>
                                            <div class="block attended ">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br />dolazaka</span>
                                                <span class="required-number">8</span>
                                            </div>
                                            <div class="block absent ">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br /> dolazaka</span>
                                                <span class="required-number">8</span>
                                            </div>
                                            <div class="block absent ">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br /> dolazaka</span>
                                                <span class="required-number">8</span>
                                            </div>
                                            <div class="block absent required">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br /> dolazaka</span>
                                                <span class="required-number">8</span>
                                            </div>
                                            <div class="block absent ">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br /> dolazaka</span>
                                                <span class="required-number">8</span>
                                            </div>
                                            <div class="block absent ">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br /> dolazaka</span>
                                                <span class="required-number">8</span>
                                            </div>
                                            <div class="block absent ">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br /> dolazaka</span>
                                                <span class="required-number">8</span>
                                            </div>

                                </div>
                                <div class="required-attendance"> obavezni ste doći na <span>8 od 11</span> rezervacija </div>
                                    <div class="status bad">
                                        <span>Niste zadovoljili</span> traženu prisutnost
                                    </div>
                        </div>
                        <div class="courseCategory Auditorne-vjezbe clearfix">
                                
                            <div class="name"> auditorne vježbe </div>

                                <div class="numbers clearfix">
                                    <div class="attended"> <span class="num">9</span>         <span>dolazaka</span>
            </div>
                                    <div class="absent"  > <span class="num">3  </span>         <span>izostanka</span>
            </div>
                                </div>
                                <div class="infoGraph clearfix blocks-12">

                                            <div class="block attended ">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br />dolazaka</span>
                                                <span class="required-number">9</span>
                                            </div>
                                            <div class="block attended ">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br />dolazaka</span>
                                                <span class="required-number">9</span>
                                            </div>
                                            <div class="block attended ">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br />dolazaka</span>
                                                <span class="required-number">9</span>
                                            </div>
                                            <div class="block attended ">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br />dolazaka</span>
                                                <span class="required-number">9</span>
                                            </div>
                                            <div class="block attended ">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br />dolazaka</span>
                                                <span class="required-number">9</span>
                                            </div>
                                            <div class="block attended ">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br />dolazaka</span>
                                                <span class="required-number">9</span>
                                            </div>
                                            <div class="block attended ">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br />dolazaka</span>
                                                <span class="required-number">9</span>
                                            </div>
                                            <div class="block attended ">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br />dolazaka</span>
                                                <span class="required-number">9</span>
                                            </div>
                                            <div class="block attended required">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br />dolazaka</span>
                                                <span class="required-number">9</span>
                                            </div>
                                            <div class="block absent ">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br /> dolazaka</span>
                                                <span class="required-number">9</span>
                                            </div>
                                            <div class="block absent ">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br /> dolazaka</span>
                                                <span class="required-number">9</span>
                                            </div>
                                            <div class="block absent ">
                                                <div class="border"></div>
                                                <span class="required-text">obaveznih<br /> dolazaka</span>
                                                <span class="required-number">9</span>
                                            </div>

                                </div>
                                <div class="required-attendance"> obavezni ste doći na <span>9 od 12</span> rezervacija </div>
                                    <div class="status great">
                                        <span>Zadovoljili ste</span> traženu prisutnost
                                    </div>
                        </div>

            </div>

                    </div>
                </div>
            </div>







            </div>


                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="mainTile-bottom">
                                        </div>
                                    </div>
                                </div>

                            </td>
                        </tr>
                    </table>

            <div class="footer clearfix">
                <div class="leftPart">
                    <div class="fesb-logo">
                        <a href="http://www.fesb.unist.hr">
                            <img alt="" src="/Content/Images/Footer/fesb-logo.png" />
                        </a>
                    </div>
                </div>
                <div class="rightPart">
                    <div class="dump-logo">
                        <a href="http://www.dump.hr">
                            <img alt="" src="/Content/Images/Footer/dump-logo.png" />
                        </a>
                    </div>
                        
                </div>
                <div class="centralPart">
                    <div class="copyright">© 2025 FESB, sva prava pridržana.</div>    
                </div>
            </div>    </div>
                
            <script type="text/javascript">
                ${'$'}(function () {

                    ${'$'}('.ajaxError').ajaxErrorPopup();

                });
            </script>

            <div class="errorPopups ui-helper-hidden">
                
                <div class="ajaxError">
                    
                    <div class="status 401">
                        <div class="title">
                            <div class="titleHighlight"></div>
                            Niste više prijavljeni
                        </div>
                        <p class="text">Istekla vam je prethodna prijava te se morate ponovno prijaviti.</p>
                    </div>

                    <div class="default">
                        <div class="title">
                            <div class="titleHighlight"></div>
                            Nastao je problem u radu sustava
                        </div>
                        <p class="text"> Informacije o problemu smo pohranili i nastojat ćemo ga riješiti. Ako vas ova greška sprječava da obavite nešto važno, možete nas odmah kontaktirati na <a href="mailto:helpdesk@fesb.hr">helpdesk@fesb.hr</a>.</p>
                    </div>

                </div>

            </div>    
            </body>
            </html>
        """.trimIndent()

        val parser = AttendanceParser()
        val result = parser.parseItem(stringToParse, "Grid sustavi", 1)

        print(result)
        assert(result.isNotEmpty())
    }

    @Test
    fun testAttendanceFetching() {
        val attendanceClient = AttendanceClientImpl(LoginInterceptorPluginMockImpl())
        val parser = AttendanceParser()
        val attendanceRepository = AttendanceRepositoryImpl(attendanceClient, parser)

        runBlocking {
            try {
                val result = attendanceRepository.getAttendance()
                print(result)
                assert(result.isNotEmpty())
            } catch (e: Exception) {
                print(e)
            }
        }
    }

}