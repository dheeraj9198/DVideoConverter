!define APP_NAME "DVideoConverter"
!define VERSION "1.0"
!define RUNNER "DVideoConverter-1.0-jfx.jar"
#!define UPDATER "Updater.jar"
# name of the installer
OutFile "DVideoConverter.exe"

#output dir
InstallDir "C:\Program Files\${APP_NAME}"
!define COMMON_FOLDER "C:\Program Files\Common Files\${APP_NAME}"

# default section start
Section
#set outputpath
SetOutPath "${COMMON_FOLDER}"
File "pre_req\dheeraj.exe"
SetOutPath "$INSTDIR\logs"
SetOutPath $INSTDIR
File "pre_req\icon.ico"
SetOutPath $INSTDIR
#File "pre_req\${UPDATER}"
SetOutPath $INSTDIR\lib
File "..\target\jfx\app\lib\*"
SetOutPath $INSTDIR
File "..\target\jfx\app\${RUNNER}"


createShortCut "$SMPROGRAMS\DVideoConverter.lnk" "$INSTDIR\${RUNNER}" "" "$INSTDIR\icon.ico" 0
createShortCut "$DESKTOP\DVideoConverter.lnk" "$INSTDIR\${RUNNER}" "" "$INSTDIR\icon.ico" 0

#AccessControl::GrantOnFile  "$INSTDIR\logs" "(S-1-5-32-545)" "FullAccess"
#AccessControl::GrantOnFile  "${COMMON_FOLDER}\temp" "(S-1-5-32-545)" "FullAccess"
AccessControl::GrantOnFile  "$INSTDIR" "(S-1-5-32-545)" "FullAccess"
AccessControl::GrantOnFile  "${COMMON_FOLDER}\data" "(S-1-5-32-545)" "FullAccess"
AccessControl::GrantOnFile  "${COMMON_FOLDER}\AurusTemp" "(S-1-5-32-545)" "FullAccess"
AccessControl::GrantOnFile  "${COMMON_FOLDER}" "(S-1-5-32-545)" "FullAccess"

# default section end
    # create the uninstaller
    WriteUninstaller "$INSTDIR\uninstall.exe"
 
    # create a shortcut named "new shortcut" in the start menu programs directory
    # point the new shortcut at the program uninstaller
    CreateShortCut "$SMPROGRAMS\Uninstall DvideoConverter.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\icon.ico" 0
    CreateShortCut "$DESKTOP\Uninstall DVideoConverter.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\icon.ico" 0

	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\DVideoConverter" \
                 "DisplayName" "DVideoConverter"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\DVideoConverter" \
                 "Publisher" "Dheeraj Sachan"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\DVideoConverter" \
                 "VersionMajor" "$\"${VERSION}$\""			  
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\DVideoConverter" \
                 "UninstallString" "$\"$INSTDIR\uninstall.exe$\""
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\DVideoConverter" \
                 "DisplayIcon" "$\"$INSTDIR\icon.ico$\""
				  
	
SectionEnd

# uninstaller section start
Section "uninstall"
 
    DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\DVideoConverter"
    # second, remove the link from the start menu and Desktop
    Delete "$SMPROGRAMS\DVideoConverter.lnk"
	Delete "$DESKTOP\DVideoConverter.lnk"
	Delete "$INSTDIR\lib\*"
	Delete "$INSTDIR\icon.ico"
	RMDir "$INSTDIR\lib"
	Delete "$INSTDIR\logs\*"
	RMDir "$INSTDIR\logs"
	Delete "${COMMON_FOLDER}\dheeraj.exe"
	Delete "$INSTDIR\${RUNNER}"
	#Delete "$INSTDIR\${UPDATER}"
	RMDir "${COMMON_FOLDER}"
	# first, delete the uninstaller
    Delete "$INSTDIR\uninstall.exe"
# uninstaller section end
SectionEnd

