$(document).ready(function() {

  // init sound plugin
  $.ionSound({
    sounds : [ "bell_ring", "button_click", "button_click_on", "button_tiny", "cd_tray", "whoop", "space", "welcome", "zoidberg2" ],
    path : "/public/sounds/"
  });

  // some helpers
  function goToMyLink(event) {
    event.stopPropagation();
    window.location.href = $(event.currentTarget).attr('data-myLink');
  }

  function confirmDialog() {
    $.ionSound.play("bell_ring");
    var conf = confirm("Are you sure?");

    if (conf == true) {
      return true;
    } else {
      return false;
    }
  }
  
  /*
   * my trick to use unused fuctions, this function is not used inside this scope
   * and compressing utils would delete it, this way I force them to keep my functions
   * here even the tools would like to remove them.
   */
  window['confirmDialog']=confirmDialog;

  /* easter egg 1 */
  var mineTimer;
  $('#myLogo').hover(function() {
    mineTimer = setTimeout(function() {
      $.ionSound.play("space");
    }, 3000);
  }, function() {
    clearTimeout(mineTimer);
    $.ionSound.stop("space");
  });

  /* easter egg 2 */
  var allKeys = [];
  var zoidberg = "90,79,73,68,66,69,82,71";
  $(document).keydown(function(e) {
    allKeys.push(e.keyCode);
    if (allKeys.toString().indexOf(zoidberg) >= 0) {
      $(document).unbind('keydown', arguments.callee);
      $.ionSound.play("whoop");
      $('#zoidberg').fadeIn(100, function() {
        $(this).animate({
          "left" : "100%"
        }, 2500, function() {
          $(this).hide();
        });
      });
      $('#cat').show(function() {
        $(this).animate({
          "left" : "100%"
        }, 3000, function() {
          $(this).hide();
          setTimeout(function() {
            $('#cat2').css('left',$(window).width()+300);
            $('#cat2').css('top',$(window).height()-300);
            $('#zoidberg2').css('left',$(window).width()+600);
            $('#zoidberg2').css('top',$(window).height()-300);
            
            $('#cat2').show(function() {
              $(this).animate({
                "left" : "-300"
              },3200, function() {
                $(this).hide();
              });
            });

            $('#zoidberg2').show(function() {
              $(this).animate({
                "left" : "-470"
              },4000, function() {
                $(this).hide();
              });
            });
            
            $.ionSound.play("zoidberg2");
          }, 4000);
        });
      });
    }
  });

  // my events and actions

  // change dropdown behaviour
  $('.ui.dropdown').dropdown({
    on : 'hover',
    transition : 'scale',
    duration : 200
  });

  // make dropdown intems hyperlinks
  $('#makeLinks').on('click', '*', goToMyLink);

  // sounds on some events
  if ($('#error_sound').length) {
    $.ionSound.play("bell_ring");
  }

  if ($('#welcome_sound').length) {
    $.ionSound.play("welcome");
  }

  // animations and sounds on some evetns
  $('.ui.checkbox').checkbox();
  $('.ui.checkbox').click(function() {
    $.ionSound.play("button_tiny");
  });

  $('#statusText').click(function() {
    if (!$('#statusButton').is(':visible')) {
      $('#statusText').removeClass("ui one field").addClass("ui two fields");
      $.ionSound.play("cd_tray");
      $('#statusButton').show(1000);
    }
  });

  $('#blogTitle').click(function() {
    if (!$('#blogContent').is(':visible')) {
      $.ionSound.play("cd_tray");
      $('#blogContent').slideDown(200, function() {
        $('#blogButton').show(200);
      });
    }
  });

  $('#commentSubject').click(function() {
    if (!$('#commentContent').is(':visible')) {
      $.ionSound.play("cd_tray");
      $('#commentContent').slideDown(200, function() {
        $('#commentButton').show(200);
      });
    }
  });

  $('#loginButton').click(function() {
    $.ionSound.stop("welcome");
    $.ionSound.play("button_click");
    $('#loginMenuItem').addClass('active');
    $('#welcomeSection').hide(1000);
    $('#loginSection').show(2000);
  });

  $('#signupButton').click(function() {
    $.ionSound.stop("welcome");
    $.ionSound.play("button_click");
    $('#signupMenuItem').addClass('active');
    $('#welcomeSection').hide(1000);
    $('#signupSection').show(2000);
  });
});