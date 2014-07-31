!define APP_NAME "ExpressMediaUploader"
!define VERSION "2.1"
!define RUNNER "ExpressMediaUploader-1.0-SNAPSHOT-jfx.jar"
!define UPDATER "Updater.jar"
# name of the installer
OutFile "ExpressMediaUploader.exe"

#output dir
InstallDir "C:\Program Files\Aurus Network\${APP_NAME}"
!define COMMON_FOLDER "C:\Program Files\Common Files\Aurus Network\${APP_NAME}"
 

# default section start
Section
#set outputpath
SetOutPath "${COMMON_FOLDER}"
File "pre_req\dheeraj.exe"
SetOutPath "$INSTDIR\logs"
SetOutPath "${COMMON_FOLDER}\data"
SetOutPath "${COMMON_FOLDER}\AurusTemp"
SetOutPath $INSTDIR
File "pre_req\icon.ico"
SetOutPath $INSTDIR
File "pre_req\${UPDATER}"
SetOutPath $INSTDIR\lib
File "..\target\jfx\app\lib\*"
SetOutPath $INSTDIR
File "..\target\jfx\app\${RUNNER}"


createShortCut "$SMPROGRAMS\ExpressMediaUploader.lnk" "$INSTDIR\${RUNNER}" "" "$INSTDIR\icon.ico" 0
createShortCut "$DESKTOP\ExpressMediaUploader.lnk" "$INSTDIR\${RUNNER}" "" "$INSTDIR\icon.ico" 0

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
    CreateShortCut "$SMPROGRAMS\Uninstall ExpressMediaUploader.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\icon.ico" 0
    CreateShortCut "$DESKTOP\Uninstall ExpressMediaUploader.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\icon.ico" 0

	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Coursehub ExpressMediaUploader" \
                 "DisplayName" "Coursehub ExpressMediaUploader"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Coursehub ExpressMediaUploader" \
                 "Publisher" "Aurus Network"			 
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Coursehub ExpressMediaUploader" \
                 "VersionMajor" "$\"${VERSION}$\""			  
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Coursehub ExpressMediaUploader" \
                 "UninstallString" "$\"$INSTDIR\uninstall.exe$\""
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Coursehub ExpressMediaUploader" \
                 "DisplayIcon" "$\"$INSTDIR\icon.ico$\""
				  
	
SectionEnd

# uninstaller section start
Section "uninstall"
 
    DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Coursehub ExpressMediaUploader"
    # second, remove the link from the start menu and Desktop
    Delete "$SMPROGRAMS\ExpressMediaUploaderlnk"
	Delete "$DESKTOP\ExpressMediaUploader.lnk"
	Delete "$INSTDIR\lib\*"
	Delete "$INSTDIR\icon.ico"
	RMDir "$INSTDIR\lib"
	Delete "$INSTDIR\logs\*"
	Delete "${COMMON_FOLDER}\AurusTemp\*"
	RMDir "${COMMON_FOLDER}\AurusTemp"
	Delete "${COMMON_FOLDER}\data\*"
	RMDir "${COMMON_FOLDER}\data"
	RMDir "$INSTDIR\logs"
	Delete "${COMMON_FOLDER}\dheeraj.exe"
	Delete "$INSTDIR\${RUNNER}"
	Delete "$INSTDIR\${UPDATER}"
	RMDir "${COMMON_FOLDER}"
	# first, delete the uninstaller
    Delete "$INSTDIR\uninstall.exe"
# uninstaller section end
SectionEnd

